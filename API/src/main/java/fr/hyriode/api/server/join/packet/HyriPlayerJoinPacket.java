package fr.hyriode.api.server.join.packet;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:30
 */
public class HyriPlayerJoinPacket extends HyriJoinPacket {

    private final UUID playerId;

    public HyriPlayerJoinPacket(String targetServer, UUID playerId) {
        super(targetServer);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

}