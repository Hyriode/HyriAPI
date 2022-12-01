package fr.hyriode.api.server.join.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:30
 */
public class PlayerJoinPacket extends HyriPacket {

    private final String targetServer;
    private final UUID playerId;

    public PlayerJoinPacket(String targetServer, UUID playerId) {
        this.targetServer = targetServer;
        this.playerId = playerId;
    }

    public String getTargetServer() {
        return this.targetServer;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

}
