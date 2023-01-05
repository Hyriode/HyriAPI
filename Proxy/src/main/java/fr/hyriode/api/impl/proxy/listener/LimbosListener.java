package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.limbo.HyggLimboStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.limbo.HyggLimboStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStoppedEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

/**
 * Created by AstFaster
 * on 17/11/2022 at 18:58
 */
public class LimbosListener {

    @SuppressWarnings("deprecation")
    public void register() {
        final HyggEventBus eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();

        eventBus.subscribe(HyggLimboStartedEvent.class, event -> {
            final String limboName = event.getLimbo().getName();
            final ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(limboName, new InetSocketAddress(limboName, 25565), "", false);

            ProxyServer.getInstance().getServers().put(limboName, serverInfo);

            HyriAPI.get().log("Added '" + limboName + "' limbo");
        });
        eventBus.subscribe(HyggLimboStoppedEvent.class, event -> {
            final String limboName = event.getLimbo().getName();

            ProxyServer.getInstance().getServers().remove(limboName);

            HyriAPI.get().log("Removed '" + limboName + "' limbo");
        });
    }

}
