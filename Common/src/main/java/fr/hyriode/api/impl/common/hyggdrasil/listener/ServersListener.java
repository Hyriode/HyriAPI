package fr.hyriode.api.impl.common.hyggdrasil.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.server.HyriServerManager;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerUpdatedEvent;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 22:24
 */
public class ServersListener {

    private final HyggEventBus eventBus;
    private final HyriServerManager serverManager;

    public ServersListener() {
        this.eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();
        this.serverManager = (HyriServerManager) HyriAPI.get().getServerManager();
    }

    public void register() {
        this.eventBus.subscribe(HyggServerStartedEvent.class, event -> this.serverManager.addServer(event.getServer()));
        this.eventBus.subscribe(HyggServerUpdatedEvent.class, event -> this.serverManager.addServer(event.getServer()));
        this.eventBus.subscribe(HyggServerStoppedEvent.class, event -> this.serverManager.removeServer(event.getServer().getName()));
    }

}
