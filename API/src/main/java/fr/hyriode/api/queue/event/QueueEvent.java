package fr.hyriode.api.queue.event;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.queue.IHyriQueue;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 15:12.<br>
 *
 * The abstraction of what an event triggered by the queue system is.
 */
public abstract class QueueEvent extends HyriEvent {

    private final UUID queueId;

    public QueueEvent(IHyriQueue queue) {
        this.queueId = queue.getId();
    }

    public IHyriQueue getQueue() {
        return HyriAPI.get().getQueueManager().getQueue(this.queueId);
    }

    public UUID getQueueId() {
        return this.queueId;
    }

}
