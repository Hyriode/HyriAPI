package fr.hyriode.api.impl.common.hyggdrasil.listener;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.proxy.HyriProxyManager;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.proxy.HyggProxyStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.proxy.HyggProxyStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.proxy.HyggProxyUpdatedEvent;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/05/2022 at 19:31
 */
public class HyriProxiesListener {

    private final HyggEventBus eventBus;

    private final HyriProxyManager proxyManager;

    public HyriProxiesListener(HyriCommonImplementation implementation) {
        this.eventBus = implementation.getHyggdrasilManager().getHyggdrasilAPI().getEventBus();
        this.proxyManager = implementation.getProxyManager();
    }

    public void register() {
        this.eventBus.subscribe(HyggProxyStartedEvent.class, event -> this.proxyManager.addProxy(event.getProxy()));
        this.eventBus.subscribe(HyggProxyUpdatedEvent.class, event -> this.proxyManager.addProxy(event.getProxy()));
        this.eventBus.subscribe(HyggProxyStoppedEvent.class, event -> this.proxyManager.removeProxy(event.getProxy()));
    }

}
