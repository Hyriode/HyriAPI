package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.event.PlayerJoinQueueEvent;
import fr.hyriode.api.queue.event.PlayerLeaveQueueEvent;
import fr.hyriode.api.queue.event.QueueDisabledEvent;
import fr.hyriode.api.queue.event.QueueUpdatedEvent;

import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 16/07/2022 at 10:04
 */
public class QueueListener {

    private final HyriQueueManager queueManager;

    public QueueListener(HyriQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @HyriEventHandler
    public void onJoin(PlayerJoinQueueEvent event) {
        this.triggerHandlers(handler -> handler.onJoinQueue(event));
    }

    @HyriEventHandler
    public void onLeave(PlayerLeaveQueueEvent event) {
        this.triggerHandlers(handler -> handler.onLeaveQueue(event));
    }

    @HyriEventHandler
    public void onUpdate(QueueUpdatedEvent event) {
        this.triggerHandlers(handler -> handler.onUpdate(event));
    }

    @HyriEventHandler
    public void onDisable(QueueDisabledEvent event) {
        this.triggerHandlers(handler -> handler.onDisable(event));
    }

    private void triggerHandlers(Consumer<IHyriQueueHandler> handlerConsumer) {
        for (IHyriQueueHandler handler : this.queueManager.getHandlers()) {
            handlerConsumer.accept(handler);
        }
    }

}
