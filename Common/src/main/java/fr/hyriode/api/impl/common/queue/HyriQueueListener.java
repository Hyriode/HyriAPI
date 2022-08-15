package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.hylios.api.queue.QueueGroup;
import fr.hyriode.hylios.api.queue.event.QueueAddEvent;
import fr.hyriode.hylios.api.queue.event.QueueRemoveEvent;
import fr.hyriode.hylios.api.queue.event.QueueUpdateGroupEvent;
import fr.hyriode.hylios.api.queue.server.event.SQueueAddEvent;
import fr.hyriode.hylios.api.queue.server.event.SQueueRemoveEvent;
import fr.hyriode.hylios.api.queue.server.event.SQueueUpdateGroupEvent;

import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 16/07/2022 at 10:04
 */
public class HyriQueueListener {

    private final HyriQueueManager queueManager;

    public HyriQueueListener(HyriQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @HyriEventHandler
    public void onAdd(QueueAddEvent event) {
        this.checkParty(event.getGroup(), handler -> handler.onPartyAdd(event), handler -> handler.onPlayerAdd(event));
    }

    @HyriEventHandler
    public void onRemove(QueueRemoveEvent event) {
        this.checkParty(event.getGroup(), handler -> handler.onPartyRemove(event), handler -> handler.onPlayerRemove(event));
    }

    @HyriEventHandler
    public void onGroupUpdate(QueueUpdateGroupEvent event) {
        this.triggerHandlers(handler -> handler.onPartyUpdate(event));
    }

    @HyriEventHandler
    public void onAdd(SQueueAddEvent event) {
        this.checkParty(event.getGroup(), handler -> handler.onPartyAdd(event), handler -> handler.onPlayerAdd(event));
    }

    @HyriEventHandler
    public void onRemove(SQueueRemoveEvent event) {
        this.checkParty(event.getGroup(), handler -> handler.onPartyRemove(event), handler -> handler.onPlayerRemove(event));
    }

    @HyriEventHandler
    public void onGroupUpdate(SQueueUpdateGroupEvent event) {
        this.triggerHandlers(handler -> handler.onPartyUpdate(event));
    }

    private void checkParty(QueueGroup group, Consumer<IHyriQueueHandler> onParty, Consumer<IHyriQueueHandler> onPlayer) {
        this.triggerHandlers(handler -> {
            if (group == null) {
                return;
            }

            if (IHyriParty.get(group.getId()) != null) {
                onParty.accept(handler);
            } else {
                onPlayer.accept(handler);
            }
        });
    }

    private void triggerHandlers(Consumer<IHyriQueueHandler> handlerConsumer) {
        for (IHyriQueueHandler handler : this.queueManager.getHandlers()) {
            handlerConsumer.accept(handler);
        }
    }

}
