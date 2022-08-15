package fr.hyriode.api.queue;

import fr.hyriode.hylios.api.queue.event.QueueAddEvent;
import fr.hyriode.hylios.api.queue.event.QueueRemoveEvent;
import fr.hyriode.hylios.api.queue.event.QueueUpdateGroupEvent;
import fr.hyriode.hylios.api.queue.packet.QueueInfoPacket;
import fr.hyriode.hylios.api.queue.server.event.SQueueAddEvent;
import fr.hyriode.hylios.api.queue.server.event.SQueueRemoveEvent;
import fr.hyriode.hylios.api.queue.server.event.SQueueUpdateGroupEvent;
import fr.hyriode.hylios.api.queue.server.packet.SQueueInfoPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:19
 */
public interface IHyriQueueHandler {

    /**
     * Triggered when information about a queue is received
     *
     * @param info The information of the queue
     */
    default void onQueueInfo(QueueInfoPacket info) {}

    /**
     * Triggered when information about a queue is received
     *
     * @param info The information of the queue
     */
    default void onQueueInfo(SQueueInfoPacket info) {}

    /**
     * Triggered when a player is added in a queue
     *
     * @param event The triggered event
     */
    default void onPlayerAdd(QueueAddEvent event) {}

    /**
     * Triggered when a player is removed from a queue
     *
     * @param event The triggered event
     */
    default void onPlayerRemove(QueueRemoveEvent event) {}

    /**
     * Triggered when a party is added in a queue
     *
     * @param event The triggered event
     */
    default void onPartyAdd(QueueAddEvent event) {}

    /**
     * Triggered when a party is removed from a queue
     *
     * @param event The triggered event
     */
    default void onPartyRemove(QueueRemoveEvent event) {}

    /**
     * Triggered when a party is updated in a queue
     *
     * @param event The triggered event
     */
    default void onPartyUpdate(QueueUpdateGroupEvent event) {}

    /**
     * Triggered when a player is added in a queue
     *
     * @param event The triggered event
     */
    default void onPlayerAdd(SQueueAddEvent event) {}

    /**
     * Triggered when a player is removed from a queue
     *
     * @param event The triggered event
     */
    default void onPlayerRemove(SQueueRemoveEvent event) {}

    /**
     * Triggered when a party is added in a queue
     *
     * @param event The triggered event
     */
    default void onPartyAdd(SQueueAddEvent event) {}

    /**
     * Triggered when a party is removed from a queue
     *
     * @param event The triggered event
     */
    default void onPartyRemove(SQueueRemoveEvent event) {}

    /**
     * Triggered when a party is updated in a queue
     *
     * @param event The triggered event
     */
    default void onPartyUpdate(SQueueUpdateGroupEvent event) {}

}
