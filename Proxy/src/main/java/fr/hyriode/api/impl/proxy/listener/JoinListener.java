package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.HyriAPIPlugin;
import fr.hyriode.api.impl.proxy.player.PlayerLoader;
import fr.hyriode.api.impl.proxy.task.OnlinePlayersTask;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.event.PlayerJoinNetworkEvent;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.server.reconnection.IHyriReconnectionData;
import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class JoinListener implements Listener {

    private final List<UUID> reconnections = new ArrayList<>();
    private final List<UUID> firstLogins = new ArrayList<>();


    private final PlayerLoader playerLoader;
    private final OnlinePlayersTask onlineTask;

    public JoinListener(HyriAPIPlugin plugin) {
        this.playerLoader = plugin.getPlayerLoader();
        this.onlineTask = plugin.getOnlinePlayersTask();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(PreLoginEvent event) {
        try {
            final PendingConnection connection = event.getConnection();
            final String name = connection.getName();
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

            UUID playerId = connection.getUniqueId();
            IHyriPlayer account = IHyriPlayer.get(playerId);

            if (account != null) {
                if (playerManager.isOnline(playerId)) { // Player is already online (but we are still checking)
                    this.onlineTask.addPlayerToCheck(playerId);

                    event.setCancelled(true);
                    event.setCancelReason(MessageUtil.ALREADY_ONLINE);
                    return;
                } else if (!account.isPremium()) { // Player is not premium but his name might have been taken
                    final PlayerLoader.MojangProfile mojangProfile = this.playerLoader.fetchMojangProfile(name);

                    if (mojangProfile.isPremium()) { // Mojang tells the queried name is now owned by a premium user. The crack player must transfer his account.
                        event.setCancelled(true);
                        event.setCancelReason(MessageUtil.PROFILE_TAKEN);
                        return;
                    }
                }
            } else { // Player doesn't exist so a new account need to be created
                final PlayerLoader.MojangProfile mojangProfile = this.playerLoader.fetchMojangProfile(name); // Queries Mojang to check if the player is premium

                playerId = mojangProfile.getPlayerId();
                account = playerManager.createPlayer(mojangProfile.isPremium(), playerId, name);

                this.firstLogins.add(playerId);
            }

            this.playerLoader.loadPlayerAccount(account, name);

            event.setEncrypting(account.isPremium());
        } catch (Exception e) {
            e.printStackTrace();

            event.setCancelled(true);
            event.setCancelReason(MessageUtil.PROFILE_ERROR);
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            final UUID playerId = player.getUniqueId();
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
            final IHyriPlayer account = IHyriPlayer.get(playerId);
            final IHyriMaintenance maintenance = network.getMaintenance();

            event.setCancelled(true);

            if (account.getRank().isSuperior(HyriStaffRankType.DESIGNER) || HyriAPI.get().getPlayerManager().getWhitelistManager().isWhitelisted(player.getName())) {
                this.connectToNetwork(player, account, event);
                return;
            }

            if (maintenance.isActive()) {
                player.disconnect(MessageUtil.createMaintenanceMessage(maintenance));
            } else if (network.getPlayerCounter().getPlayers() >= network.getSlots() && account.getRank().isDefault()) {
                player.disconnect(MessageUtil.SERVER_FULL_MESSAGE);
            } else {
                this.connectToNetwork(player, account, event);
            }
        }
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final IHyriPlayerSession session = IHyriPlayerSession.get(playerId);

        if (session == null) {
            player.disconnect(MessageUtil.PROFILE_ERROR);
            return;
        }

        session.setLastServer(session.getServer());
        session.setServer(event.getServer().getInfo().getName());
        session.update();

        if (!this.reconnections.remove(playerId)) {
            return;
        }

        // Reconnection part (if the player was in a game)
        final IHyriReconnectionData reconnectionData = HyriAPI.get().getServerManager().getReconnectionHandler().get(playerId);

        if (reconnectionData != null) {
            reconnectionData.reconnect();
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        this.playerLoader.handleDisconnection(event.getPlayer());
    }

    private void connectToNetwork(ProxiedPlayer player, IHyriPlayer account, ServerConnectEvent event) {
        final UUID playerId = player.getUniqueId();

        ServerInfo serverInfo = null;
        if (account.isPremium()) { // If he is premium, connect him directly to a lobby
            final HyggServer lobby = HyriAPI.get().getLobbyAPI().getBestLobby();

            if (lobby != null) {
                // Load reconnection (if exists) before sending player to the lobby
                final IHyriReconnectionData reconnectionData = HyriAPI.get().getServerManager().getReconnectionHandler().get(playerId);

                if (reconnectionData != null) {
                    this.reconnections.add(playerId);
                }

                serverInfo = ProxyServer.getInstance().getServerInfo(lobby.getName());
            }
        } else { // If he is crack, connect him first to a limbo to authenticate
            final HyggLimbo limbo = HyriAPI.get().getLimboManager().getBestLimbo(HyggLimbo.Type.LOGIN);

            if (limbo != null) {
                serverInfo = ProxyServer.getInstance().getServerInfo(limbo.getName());
            }
        }

        if (serverInfo == null) {
            player.disconnect(MessageUtil.NO_SERVER_MESSAGE);
            return;
        }

        event.setTarget(serverInfo);
        event.setCancelled(false);

        HyriAPI.get().getProxy().addPlayer(playerId);
        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerJoinNetworkEvent(playerId));
    }

}
