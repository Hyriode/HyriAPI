package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class HyriJoinListener implements Listener {

    private static final String WARNING = "\u26A0";

    private final HyriCommonImplementation implementation;
    private final HyggdrasilManager hyggdrasilManager;

    public HyriJoinListener(HyriCommonImplementation implementation, HyggdrasilManager hyggdrasilManager) {
        this.implementation = implementation;
        this.hyggdrasilManager = hyggdrasilManager;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        try {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final PendingConnection connection = event.getConnection();
            final UUID uuid = connection.getUniqueId();

            IHyriPlayer player = playerManager.getPlayer(uuid);
            if (player == null) {
                player = playerManager.createPlayer(true, uuid, connection.getName());
            }

            player.setName(player.getName());
            player.setLastLoginDate(new Date(System.currentTimeMillis()));
            player.setOnline(true);
            player.setCurrentProxy(HyriAPI.get().getProxy().getName());

            player.update();

            playerManager.setPlayerId(player.getName(), player.getUniqueId());
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
            final IHyriNetwork network = HyriAPI.get().getNetwork();
            final IHyriMaintenance maintenance = network.getMaintenance();

            if (maintenance.isActive()) {
                player.disconnect(this.buildMessage("Une maintenance est actuellement en cours. Raison: " + maintenance.getReason()));
            } else if (network.getPlayers() >= network.getSlots()) {
                player.disconnect(this.buildMessage("Le serveur est plein !"));
            } else {
                final IHyriServerManager serverManager = HyriAPI.get().getServerManager();
                final HyggServer lobby = serverManager.getLobby();

                if (lobby == null) {
                    player.disconnect(this.buildMessage("Aucun lobby n'est actuellement démarré !"));
                } else {
                    event.setTarget(ProxyServer.getInstance().getServerInfo(lobby.getName()));

                    this.hyggdrasilManager.sendData();
                }
            }
        }
    }

    private TextComponent buildMessage(String reason) {
        return new TextComponent(new ComponentBuilder("◆ Hyriode ◆").color(ChatColor.DARK_AQUA)
                .append("\n")
                .append(WARNING + " " + reason + " " + WARNING).color(ChatColor.RED)
                .create());
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayer(player.getUniqueId());

        if (account != null) {
            account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
            account.setOnline(false);
            account.setCurrentProxy(null);
            account.update();
        }

        this.hyggdrasilManager.sendData();
    }

}
