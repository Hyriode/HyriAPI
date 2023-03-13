package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerUpdatedEvent;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

/**
 * Created by AstFaster
 * on 17/11/2022 at 18:58
 */
public class ServersListener {

    public void register() {
        final HyggEventBus eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();

        for (HyggServer server : HyriAPI.get().getServerManager().getServers()) {
            this.addServer(server.getName());
        }

        eventBus.subscribe(HyggServerStartedEvent.class, event -> this.addServer(event.getServer().getName()));
        eventBus.subscribe(HyggServerStoppedEvent.class, event -> this.removeServer(event.getServer().getName()));
    }

    @SuppressWarnings("deprecation")
    private void addServer(String server) {
        final ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(server, new InetSocketAddress(server, 25565), "", false);

        ProxyServer.getInstance().getServers().put(server, serverInfo);

        HyriAPI.get().log("Added '" + server + "' server");
    }

    @SuppressWarnings("deprecation")
    private void removeServer(String server) {
        ProxyServer.getInstance().getServers().remove(server);

        HyriAPI.get().log("Removed '" + server + "' server");
    }

}
