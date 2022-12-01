package fr.hyriode.api.queue.packet;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 15:20.<br>
 *
 * Packet used to remove a player from a queue.
 */
public class LeaveQueuePacket extends QueuePacket {

    private final UUID playerId;

    public LeaveQueuePacket(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

}
