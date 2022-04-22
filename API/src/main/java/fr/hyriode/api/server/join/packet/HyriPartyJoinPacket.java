package fr.hyriode.api.server.join.packet;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:31
 */
public class HyriPartyJoinPacket extends HyriJoinPacket {

    private final UUID partyId;

    public HyriPartyJoinPacket(String targetServer, UUID partyId) {
        super(targetServer);
        this.partyId = partyId;
    }

    public UUID getPartyId() {
        return this.partyId;
    }

}
