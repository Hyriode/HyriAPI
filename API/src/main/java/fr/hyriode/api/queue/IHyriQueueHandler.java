package fr.hyriode.api.queue;

import fr.hyriode.api.queue.event.*;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:19.<br>
 *
 * A handler can be registered to listen for incoming information about queues.
 */
public interface IHyriQueueHandler {

    /**
     * Triggered each time a player joins a queue.
     *
     * @param event The triggered {@link QueueEvent}
     */
    default void onJoinQueue(PlayerJoinQueueEvent event) {}

    /**
     * Triggered each time a player leaves a queue.
     *
     * @param event The triggered {@link QueueEvent}
     */
    default void onLeaveQueue(PlayerLeaveQueueEvent event) {}

    /**
     * Triggered each time a queue is updated
     *
     * @param event The triggered {@link QueueEvent}
     */
    default void onUpdate(QueueUpdatedEvent event) {}

    /**
     * Triggered each time a queue is disabled
     *
     * @param event The triggered {@link QueueEvent}
     */
    default void onDisable(QueueDisabledEvent event) {}

}
