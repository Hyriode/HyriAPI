package fr.hyriode.api.impl.common.hyggdrasil.listener;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.server.HyriServerManager;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerUpdatedEvent;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 22:24
 */
public class HyriServersListener {

    private final HyggEventBus eventBus;
    private final HyriServerManager serverManager;

    public HyriServersListener(HyriCommonImplementation implementation) {
        this.eventBus = implementation.getHyggdrasilManager().getHyggdrasilAPI().getEventBus();
        this.serverManager = implementation.getServerManager();
    }

    public void register() {
        this.eventBus.subscribe(HyggServerStartedEvent.class, event -> this.serverManager.addServer(event.getServer()));
        this.eventBus.subscribe(HyggServerUpdatedEvent.class, event -> this.serverManager.addServer(event.getServer()));
        this.eventBus.subscribe(HyggServerStoppedEvent.class, event -> this.serverManager.removeServer(event.getServer().getName()));
    }

}
