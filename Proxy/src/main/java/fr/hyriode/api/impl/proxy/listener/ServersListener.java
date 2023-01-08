package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStoppedEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

/**
 * Created by AstFaster
 * on 17/11/2022 at 18:58
 */
public class ServersListener {

    @SuppressWarnings("deprecation")
    public void register() {
        final HyggEventBus eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();

        eventBus.subscribe(HyggServerStartedEvent.class, event -> {
            final String serverName = event.getServer().getName();
            final ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(serverName, new InetSocketAddress(serverName, 25565), "", false);

            ProxyServer.getInstance().getServers().put(serverName, serverInfo);

            HyriAPI.get().log("Added '" + serverName + "' server");
        });
        eventBus.subscribe(HyggServerStoppedEvent.class, event -> {
            final String serverName = event.getServer().getName();

            ProxyServer.getInstance().getServers().remove(serverName);

            HyriAPI.get().log("Removed '" + serverName + "' server");
        });
    }

}
