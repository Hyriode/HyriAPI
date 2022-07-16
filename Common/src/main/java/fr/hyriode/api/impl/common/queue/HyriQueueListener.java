package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.hylios.api.queue.QueueGroup;
import fr.hyriode.hylios.api.queue.event.QueueAddEvent;
import fr.hyriode.hylios.api.queue.event.QueueRemoveEvent;
import fr.hyriode.hylios.api.queue.event.QueueUpdateGroupEvent;

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
        this.triggerHandlers(handler -> {
            final QueueGroup group = event.getGroup();

            if (IHyriParty.get(group.getId()) != null) {
                handler.onPartyAdd(event);
            } else {
                handler.onPlayerAdd(event);
            }
        });
    }

    @HyriEventHandler
    public void onRemove(QueueRemoveEvent event) {
        this.triggerHandlers(handler -> {
            final QueueGroup group = event.getGroup();

            if (IHyriParty.get(group.getId()) != null) {
                handler.onPartyRemove(event);
            } else {
                handler.onPlayerRemove(event);
            }
        });
    }

    @HyriEventHandler
    public void onGroupUpdate(QueueUpdateGroupEvent event) {
        this.triggerHandlers(handler -> handler.onPartyUpdate(event));
    }

    private void triggerHandlers(Consumer<IHyriQueueHandler> handlerConsumer) {
        for (IHyriQueueHandler handler : this.queueManager.getHandlers()) {
            handlerConsumer.accept(handler);
        }
    }

}
