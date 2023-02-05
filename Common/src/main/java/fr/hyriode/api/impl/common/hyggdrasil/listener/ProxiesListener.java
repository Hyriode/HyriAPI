package fr.hyriode.api.impl.common.hyggdrasil.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.proxy.HyriProxyManager;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.proxy.HyggProxyStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.proxy.HyggProxyStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.proxy.HyggProxyUpdatedEvent;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/05/2022 at 19:31
 */
public class ProxiesListener {

    private final HyggEventBus eventBus;
    private final HyriProxyManager proxyManager;

    public ProxiesListener() {
        this.eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();
        this.proxyManager = (HyriProxyManager) HyriAPI.get().getProxyManager();
    }

    public void register() {
        this.eventBus.subscribe(HyggProxyStartedEvent.class, event -> this.proxyManager.addProxy(event.getProxy()));
        this.eventBus.subscribe(HyggProxyUpdatedEvent.class, event -> {
            if (event.getProxy().getState() != HyggProxy.State.SHUTDOWN) {
                this.proxyManager.addProxy(event.getProxy());
            }
        });
        this.eventBus.subscribe(HyggProxyStoppedEvent.class, event -> this.proxyManager.removeProxy(event.getProxy()));
    }

}
