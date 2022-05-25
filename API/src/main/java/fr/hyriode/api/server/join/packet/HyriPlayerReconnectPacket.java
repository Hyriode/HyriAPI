package fr.hyriode.api.server.join.packet;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/05/2022 at 13:15
 */
public class HyriPlayerReconnectPacket extends HyriPlayerJoinPacket {

    public HyriPlayerReconnectPacket(String targetServer, UUID playerId) {
        super(targetServer, playerId);
    }

}
