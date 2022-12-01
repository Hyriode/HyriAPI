package fr.hyriode.api.queue.event;

import fr.hyriode.api.queue.IHyriQueue;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 15:15.<br>
 *
 * Event triggered when a player joins a queue.
 */
public class PlayerJoinQueueEvent extends QueueEvent {

    private final UUID playerId;

    public PlayerJoinQueueEvent(IHyriQueue queue, UUID playerId) {
        super(queue);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

}
