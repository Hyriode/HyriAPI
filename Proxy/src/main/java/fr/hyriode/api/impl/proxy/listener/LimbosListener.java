package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.limbo.HyggLimboStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.limbo.HyggLimboStoppedEvent;
import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;
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

        for (HyggLimbo limbo : HyriAPI.get().getLimboManager().getLimbos()) {
            this.addLimbo(limbo.getName());
        }

        eventBus.subscribe(HyggLimboStartedEvent.class, event -> this.addLimbo(event.getLimbo().getName()));
        eventBus.subscribe(HyggLimboStoppedEvent.class, event -> {
            final String limboName = event.getLimbo().getName();

            ProxyServer.getInstance().getServers().remove(limboName);

            HyriAPI.get().log("Removed '" + limboName + "' limbo");
        });
    }

    @SuppressWarnings("deprecation")
    private void addLimbo(String limbo) {
        final ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(limbo, new InetSocketAddress(limbo, 25565), "", false);

        ProxyServer.getInstance().getServers().put(limbo, serverInfo);

        HyriAPI.get().log("Added '" + limbo + "' limbo");
    }

}
