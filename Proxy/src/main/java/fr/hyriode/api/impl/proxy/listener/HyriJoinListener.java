package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class HyriJoinListener implements Listener {

    private static final String WARNING = "\u26A0";

    private final HyggdrasilManager hyggdrasilManager;

    public HyriJoinListener(HyggdrasilManager hyggdrasilManager) {
        this.hyggdrasilManager = hyggdrasilManager;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            final IHyriServerManager serverManager = HyriAPI.get().getServerManager();
            final HyggServer lobby = serverManager.getLobby();

            if (lobby == null) {
                final BaseComponent[] message = new ComponentBuilder("◆ Hyriode ◆").color(ChatColor.DARK_AQUA)
                        .append("\n")
                        .append(WARNING + " Aucun lobby n'est actuellement démarré ! " + WARNING).color(ChatColor.RED)
                        .create();

                player.disconnect(new TextComponent(message));
            } else {
                event.setTarget(ProxyServer.getInstance().getServerInfo(lobby.getName()));

                this.hyggdrasilManager.sendData();
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        this.hyggdrasilManager.sendData();
    }

}
