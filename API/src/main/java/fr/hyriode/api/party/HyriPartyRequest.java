package fr.hyriode.api.party;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class HyriPartyRequest extends HyriPacket {

    public static final String REDIS_CHANNEL = "party_request";

    private final UUID partyId;
    private final UUID sender;
    private final UUID receiver;

    public HyriPartyRequest(UUID partyId, UUID sender, UUID receiver) {
        this.partyId = partyId;
        this.sender = sender;
        this.receiver = receiver;
    }

    public UUID getPartyId() {
        return this.partyId;
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID getReceiver() {
        return this.receiver;
    }
}
