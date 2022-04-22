package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.proxy.loader.HyriFriendsLoader;
import fr.hyriode.api.impl.proxy.loader.HyriPlayerLoader;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
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

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class HyriJoinListener implements Listener {

    private final HyriPlayerLoader playerLoader;
    private final HyriFriendsLoader friendsLoader;

    private final HyggdrasilManager hyggdrasilManager;

    public HyriJoinListener(HyriCommonImplementation api) {
        this.hyggdrasilManager = api.getHyggdrasilManager();
        this.playerLoader = new HyriPlayerLoader(api.getHydrionManager());
        this.friendsLoader = new HyriFriendsLoader(api.getHydrionManager());
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        try {
            final PendingConnection connection = event.getConnection();
            final UUID uuid = connection.getUniqueId();
            final String name = connection.getName();
            final IHyriPlayer account = this.playerLoader.loadPlayerAccount(uuid, name);

            if (!HyriAPI.get().getNetworkManager().getNetwork().getMaintenance().isActive() || account.getRank().isStaff()) {
                this.friendsLoader.loadFriends(uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();

            event.setCancelled(true);
            event.setCancelReason(TextComponent.fromLegacyText(ChatColor.RED + "An error occurred while loading your profile!"));
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
            final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());
            final IHyriMaintenance maintenance = network.getMaintenance();

            event.setCancelled(true);

            if (account.getRank().isStaff()) {
                this.connectToLobby(player, network, event);
                return;
            }

            if (maintenance.isActive()) {
                player.disconnect(MessageUtil.createMaintenanceMessage(maintenance));
            } else if (network.getPlayerCount().getPlayers() >= network.getSlots() && account.getRank().isDefault()) {
                player.disconnect(MessageUtil.createMaintenanceMessage(maintenance));
                player.disconnect(MessageUtil.SERVER_FULL_MESSAGE);
            } else {
                this.connectToLobby(player, network, event);
            }
        }
    }

    private void connectToLobby(ProxiedPlayer player, IHyriNetwork network, ServerConnectEvent event) {
        final IHyriServerManager serverManager = HyriAPI.get().getServerManager();
        final HyggServer lobby = serverManager.getLobby();

        if (lobby == null) {
            player.disconnect(MessageUtil.NO_LOBBY_MESSAGE);
        } else {
            event.setTarget(ProxyServer.getInstance().getServerInfo(lobby.getName()));
            event.setCancelled(false);

            network.getPlayerCount().addPlayers(1);
            network.update();

            this.hyggdrasilManager.sendData();
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (player.isConnected()) {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
            final UUID uuid = player.getUniqueId();

            this.playerLoader.unloadPlayerAccount(uuid);
            this.friendsLoader.unloadFriends(uuid);

            network.getPlayerCount().removePlayers(1);
            network.update();

            this.hyggdrasilManager.sendData();
        }
    }

}
