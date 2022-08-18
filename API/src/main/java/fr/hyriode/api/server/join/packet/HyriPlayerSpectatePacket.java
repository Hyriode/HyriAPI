package fr.hyriode.api.server.join.packet;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:30
 */
public class HyriPlayerSpectatePacket extends HyriPlayerJoinPacket {

    public HyriPlayerSpectatePacket(String targetServer, UUID playerId) {
        super(targetServer, playerId);
    }

}
