package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.player.PlayerLoader;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.event.PlayerJoinNetworkEvent;
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

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class JoinListener implements Listener {

    private final Set<UUID> reconnections = ConcurrentHashMap.newKeySet();

    private final PlayerLoader playerLoader;

    public JoinListener() {
        this.playerLoader = new PlayerLoader();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(PreLoginEvent event) {
        try {
            final long before = System.nanoTime();

            final PendingConnection connection = event.getConnection();
            final String name = connection.getName();
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

            UUID playerId = playerManager.getPlayerId(name);
            PlayerLoader.MojangProfile mojangProfile = null;

            if (playerId == null) {
                mojangProfile = this.playerLoader.fetchMojangProfile(name);
                playerId = mojangProfile.getPlayerId();
            }

            IHyriPlayer account = IHyriPlayer.get(playerId);

            if (account != null) {
                if (this.playerLoader.isOnline(playerId)) { // Check if the player is not online
                    event.setCancelled(true);
                    event.setCancelReason(MessageUtil.ALREADY_ONLINE);
                    return;
                } else if (!account.getAuth().isPremium()) { // Player is not premium but his name might have been taken
                    if (mojangProfile == null) {
                        mojangProfile = this.playerLoader.fetchMojangProfile(name);
                    }

                    if (mojangProfile.isPremium()) { // Mojang tells the queried name is now owned by a premium user. The crack player must transfer his account. And we will create a new account for the premium player.
                        HyriAPI.get().getPlayerManager().savePlayerId(name, mojangProfile.getPlayerId());

                        event.setCancelled(true);
                        event.setCancelReason(MessageUtil.PROFILE_TAKEN);
                        return;
                    }
                }
            }

            if (account == null) { // Player doesn't exist so a new account need to be created
                if (!this.playerLoader.isNameValid(name)) {
                    event.setCancelled(true);
                    event.setCancelReason(MessageUtil.INVALID_NAME);
                    return;
                }

                if (mojangProfile == null) {
                    mojangProfile = this.playerLoader.fetchMojangProfile(name); // Queries Mojang to check if the player is premium
                    playerId = mojangProfile.getPlayerId();
                }

                if (mojangProfile.isPremium()) { // Only create the account if the player is premium! (for crack users it will be after being registered)
                    account = playerManager.createPlayer(true, playerId, name);
                }
            }

            this.playerLoader.loadPlayerAccount(playerId, account, name);

            event.setEncrypting(account != null && account.getAuth().isPremium());

            System.out.println("Connection took " + (System.nanoTime() - before) + "ns to process.");
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

            if (account != null && (account.getRank().isStaff() || HyriAPI.get().getPlayerManager().getWhitelistManager().isWhitelisted(player.getName()))) {
                this.connectToNetwork(player, account, event);
                return;
            }

            if (maintenance.isActive()) {
                player.disconnect(MessageUtil.createMaintenanceMessage(maintenance));
            } else if (network.getPlayerCounter().getPlayers() >= network.getSlots() && (account == null || account.getRank().isDefault())) {
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
        this.playerLoader.handleDisconnection(event.getPlayer(), false);
    }

    private void connectToNetwork(ProxiedPlayer player, IHyriPlayer account, ServerConnectEvent event) {
        final UUID playerId = player.getUniqueId();

        ServerInfo serverInfo = null;
        if (account != null && account.getAuth().isPremium()) { // If he is premium, connect him directly to a lobby
            final HyggServer lobby = HyriAPI.get().getLobbyAPI().getBestLobby();

            if (lobby != null) {
                // Load reconnection (if exists) before sending player to the lobby
                final IHyriReconnectionData reconnectionData = HyriAPI.get().getServerManager().getReconnectionHandler().get(playerId);

                if (reconnectionData != null) {
                    this.reconnections.add(playerId);
                }

                serverInfo = ProxyServer.getInstance().getServerInfo(lobby.getName());

                if (serverInfo != null) {
                    HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerJoinNetworkEvent(playerId));
                }
            }
        } else { // If he is crack, connect him first to a limbo to authenticate
            final HyggLimbo limbo = HyriAPI.get().getLimboManager().getBestLimbo(HyggLimbo.Type.LOGIN);

            if (limbo != null) {
                serverInfo = ProxyServer.getInstance().getServerInfo(limbo.getName());
            }
        }

        if (serverInfo == null) {
            this.playerLoader.handleDisconnection(player, true);

            player.disconnect(MessageUtil.NO_SERVER_MESSAGE);
            return;
        }

        event.setTarget(serverInfo);
        event.setCancelled(false);

        HyriAPI.get().getProxy().addPlayer(playerId);
    }

}
