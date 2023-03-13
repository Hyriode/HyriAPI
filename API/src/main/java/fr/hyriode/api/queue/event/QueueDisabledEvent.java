package fr.hyriode.api.queue.event;

import fr.hyriode.api.queue.IHyriQueue;

/**
 * Created by AstFaster
 * on 23/11/2022 at 15:14.<br>
 *
 * Event triggered each time a queue is disabled.
 */
public class QueueDisabledEvent extends QueueEvent {

    public QueueDisabledEvent(IHyriQueue queue) {
        super(queue);
    }

}
