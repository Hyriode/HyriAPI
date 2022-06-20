package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.impl.common.friend.HyriFriends;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.proxy.HyriAPIPlugin;
import fr.hyriode.api.impl.proxy.player.HyriPlayerLoader;
import fr.hyriode.api.impl.proxy.task.HyriOnlinePlayersTask;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.api.server.join.packet.HyriPlayerReconnectPacket;
import fr.hyriode.api.server.reconnection.IHyriReconnectionData;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class HyriJoinListener implements Listener {

    private final List<UUID> loginPlayers;
    private final List<UUID> reconnections;

    private final HyriPlayerLoader playerLoader;
    private final HyriOnlinePlayersTask onlineTask;

    private final HyggdrasilManager hyggdrasilManager;
    private final HydrionManager hydrionManager;

    public HyriJoinListener(HyriAPIPlugin plugin) {
        this.hyggdrasilManager = plugin.getAPI().getHyggdrasilManager();
        this.hydrionManager = plugin.getAPI().getHydrionManager();
        this.playerLoader = plugin.getPlayerLoader();
        this.onlineTask = plugin.getOnlinePlayersTask();
        this.loginPlayers = new ArrayList<>();
        this.reconnections = new ArrayList<>();
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        try {
            final PendingConnection connection = event.getConnection();
            final UUID uuid = connection.getUniqueId();
            final String name = connection.getName();
            IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(uuid);

            if (account != null && account.isOnline() && HyriAPI.get().getPlayerManager().getPlayerFromRedis(uuid) != null) {
                event.setCancelled(true);
                event.setCancelReason(MessageUtil.ALREADY_ONLINE);

                this.onlineTask.addPlayerToCheck(uuid);
                return;
            }

            account = this.playerLoader.loadPlayerAccount(account, uuid, name);

            if (!HyriAPI.get().getNetworkManager().getNetwork().getMaintenance().isActive() || account.getRank().isStaff() || HyriAPI.get().getPlayerManager().getWhitelistManager().isWhitelisted(name)) {
                HyriAPI.get().getFriendManager().saveFriends(HyriAPI.get().getFriendManager().createHandler(uuid));
            }
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
            final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(playerId);
            final IHyriMaintenance maintenance = network.getMaintenance();

            event.setCancelled(true);

            if (account.getRank().isSuperior(HyriStaffRankType.DESIGNER) || HyriAPI.get().getPlayerManager().getWhitelistManager().isWhitelisted(player.getName())) {
                this.connectToLobby(player, event);
                return;
            }

            if (!HyriAPI.get().getConfiguration().isProduction()) {
                player.disconnect(MessageUtil.NO_PERMISSION);
            } else if (maintenance.isActive()) {
                player.disconnect(MessageUtil.createMaintenanceMessage(maintenance));
            } else if (network.getPlayerCount().getPlayers() >= network.getSlots() && account.getRank().isDefault()) {
                player.disconnect(MessageUtil.SERVER_FULL_MESSAGE);
            } else {
                this.connectToLobby(player, event);
            }
        }
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        final UUID playerId = event.getPlayer().getUniqueId();
        final IHyriPlayer account = IHyriPlayer.get(playerId);
        final IHyriParty party = HyriAPI.get().getPartyManager().getParty(account.getParty());
        final IHyriPlayerManager pm = HyriAPI.get().getPlayerManager();

        account.setOnline(true);
        account.setLastServer(account.getCurrentServer());
        account.setCurrentServer(event.getServer().getInfo().getName());

        if (party != null && party.isLeader(playerId)) {
            party.setServer(account.getCurrentServer());
        }

        pm.savePrefix(playerId, account.getNameWithRank());
        pm.getPlayerFromHydrion(playerId).whenComplete((hydrionPlayer, throwable) -> {
            if (hydrionPlayer != null) {
                final HyriRank rank = hydrionPlayer.getRank();

                if (rank.isSuperior(account.getRank().getPlayerType())) {
                    account.setRank(rank);
                }

                final HyriPlus hyriPlus = hydrionPlayer.getHyriPlus();

                if (hyriPlus.getExpirationDate().getTime() > account.getHyriPlus().getExpirationDate().getTime()) {
                    account.setHyriPlus(hyriPlus);
                }
            }

            if (pm.getPlayerFromRedis(playerId) != null) {
                account.update();
            }

            pm.sendPlayerToHydrion(account);
        });

        account.update();

        if (!this.reconnections.remove(playerId)) {
            return;
        }

        // Reconnection part (if the player was in a game and reconnected)
        final IHyriReconnectionData reconnectionData = HyriAPI.get().getServerManager().getReconnectionHandler().get(playerId);

        if (reconnectionData != null) {
            reconnectionData.reconnect();
        }
    }

    @EventHandler
    public void onServerDisconnect(ServerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final List<IHyriFriend> friends = HyriAPI.get().getFriendManager().getFriends(playerId);

        this.hydrionManager.getClient().getFriendsModule().setFriends(player.getUniqueId(), HyriAPI.GSON.toJson(new HyriFriends(friends)));
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (this.loginPlayers.contains(uuid)) {
            this.playerLoader.unloadPlayerAccount(uuid);

            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

            network.getPlayerCount().removePlayers(1);
            network.update();

            this.hyggdrasilManager.sendData();

            this.loginPlayers.remove(uuid);
        }
    }

    private void connectToLobby(ProxiedPlayer player, ServerConnectEvent event) {
        final UUID playerId = player.getUniqueId();
        final IHyriServerManager serverManager = HyriAPI.get().getServerManager();
        final HyggServer lobby = serverManager.getLobby();

        if (lobby == null) {
            player.disconnect(MessageUtil.NO_LOBBY_MESSAGE);
        } else {
            // Load reconnection (if exists) before sending player to the lobby
            final IHyriReconnectionData reconnectionData = serverManager.getReconnectionHandler().get(playerId);

            if (reconnectionData != null) {
                this.reconnections.add(playerId);
            }

            event.setTarget(ProxyServer.getInstance().getServerInfo(lobby.getName()));
            event.setCancelled(false);

            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

            this.loginPlayers.add(playerId);

            network.getPlayerCount().addPlayers(1);
            network.update();

            this.hyggdrasilManager.sendData();
        }
    }

}
