package fr.hyriode.api.queue.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.queue.IHyriQueue;

/**
 * Created by AstFaster
 * on 23/11/2022 at 15:12.<br>
 *
 * The abstraction of what an event triggered by the queue system is.
 */
public abstract class QueueEvent extends HyriEvent {

    private final IHyriQueue queue;

    public QueueEvent(IHyriQueue queue) {
        this.queue = queue;
    }

    public IHyriQueue getQueue() {
        return this.queue;
    }

}
