package fr.hyriode.api.packet.model;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 02/01/2023 at 18:12
 */
public class PlayerLimboSendPacket extends HyriPacket {

    /** The {@link UUID} of the player to send */
    private final UUID playerId;
    /** The name of the limbo */
    private final String limboName;

    /**
     * Constructor of {@link PlayerLimboSendPacket}
     *
     * @param playerId The player {@link UUID}
     * @param limboName The name of the limbo
     */
    public PlayerLimboSendPacket(UUID playerId, String limboName) {
        this.playerId = playerId;
        this.limboName = limboName;
    }

    /**
     * Get the {@link UUID} of the player to send
     *
     * @return A player {@link UUID}
     */
    public UUID getPlayerId() {
        return this.playerId;
    }

    /**
     * Get the name of the limbo
     *
     * @return A limbo name
     */
    public String getLimboName() {
        return this.limboName;
    }

}
