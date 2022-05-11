package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.common.player.packet.HyriPlayerLeavePacket;
import fr.hyriode.api.impl.proxy.player.HyriPlayerLoader;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class HyriJoinListener implements Listener {

    private final List<UUID> loginPlayers;

    private final HyriPlayerLoader playerLoader;

    private final HyggdrasilManager hyggdrasilManager;

    public HyriJoinListener(HyriCommonImplementation api) {
        this.hyggdrasilManager = api.getHyggdrasilManager();
        this.playerLoader = new HyriPlayerLoader(api.getHydrionManager());
        this.loginPlayers = new ArrayList<>();
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

            if (account.getRank().isStaff() || HyriAPI.get().getPlayerManager().getWhitelistManager().isWhitelisted(player.getName())) {
                this.connectToLobby(player, event);
                return;
            }

            if (maintenance.isActive()) {
                player.disconnect(MessageUtil.createMaintenanceMessage(maintenance));
            } else if (network.getPlayerCount().getPlayers() >= network.getSlots() && account.getRank().isDefault()) {
                player.disconnect(MessageUtil.createMaintenanceMessage(maintenance));
                player.disconnect(MessageUtil.SERVER_FULL_MESSAGE);
            } else {
                this.connectToLobby(player, event);
            }
        }
    }

    private void connectToLobby(ProxiedPlayer player, ServerConnectEvent event) {
        final IHyriServerManager serverManager = HyriAPI.get().getServerManager();
        final HyggServer lobby = serverManager.getLobby();

        if (lobby == null) {
            player.disconnect(MessageUtil.NO_LOBBY_MESSAGE);
        } else {
            event.setTarget(ProxyServer.getInstance().getServerInfo(lobby.getName()));
            event.setCancelled(false);

            HyriAPI.get().getProxy().addPlayer();

            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

            this.loginPlayers.add(player.getUniqueId());

            network.getPlayerCount().addPlayers(1);
            network.update();

            this.hyggdrasilManager.sendData();
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (this.loginPlayers.contains(uuid)) {
            final boolean result = this.playerLoader.unloadPlayerAccount(uuid);

            if (result) {
                final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

                network.getPlayerCount().removePlayers(1);
                network.update();

                HyriAPI.get().getProxy().removePlayer();

                this.hyggdrasilManager.sendData();

                HyriAPI.get().getPubSub().send(HyriChannel.SERVERS, new HyriPlayerLeavePacket(uuid));

                this.loginPlayers.remove(uuid);
            }
            return;
        }

        HyriAPI.get().getPlayerManager().removePlayer(uuid);
    }

}
