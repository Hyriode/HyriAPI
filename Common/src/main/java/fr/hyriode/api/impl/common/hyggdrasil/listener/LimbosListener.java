package fr.hyriode.api.impl.common.hyggdrasil.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.limbo.HyriLimboManager;
import fr.hyriode.api.impl.common.server.HyriServerManager;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.limbo.HyggLimboStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.limbo.HyggLimboStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.limbo.HyggLimboUpdatedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerUpdatedEvent;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 22:24
 */
public class LimbosListener {

    private final HyggEventBus eventBus;
    private final HyriLimboManager limboManager;

    public LimbosListener() {
        this.eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();
        this.limboManager = (HyriLimboManager) HyriAPI.get().getLimboManager();
    }

    public void register() {
        this.eventBus.subscribe(HyggLimboStartedEvent.class, event -> this.limboManager.addLimbo(event.getLimbo()));
        this.eventBus.subscribe(HyggLimboUpdatedEvent.class, event -> this.limboManager.addLimbo(event.getLimbo()));
        this.eventBus.subscribe(HyggLimboStoppedEvent.class, event -> this.limboManager.removeLimbo(event.getLimbo().getName()));
    }

}
