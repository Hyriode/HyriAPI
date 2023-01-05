package fr.hyriode.api.packet.model;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 23:20
 */
public class PlayerServerSendPacket extends HyriPacket {

    /** The unique id of the player to send */
    private final UUID playerUUID;
    /** The name of the server where the player will be sent */
    private final String serverName;

    /**
     * Constructor of {@link PlayerServerSendPacket}
     *
     * @param playerUUID The player's {@link UUID}
     * @param serverName The name of the concerned server
     */
    public PlayerServerSendPacket(UUID playerUUID, String serverName) {
        this.playerUUID = playerUUID;
        this.serverName = serverName;
    }

    /**
     * Get the unique id of the player to send
     *
     * @return An {@link UUID}
     */
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    /**
     * Get the name of the server where the player will be sent
     *
     * @return A server name
     */
    public String getServerName() {
        return this.serverName;
    }

}
