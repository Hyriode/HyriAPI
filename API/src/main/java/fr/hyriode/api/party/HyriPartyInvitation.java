package fr.hyriode.api.party;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class HyriPartyInvitation {

    private final UUID partyId;
    private final UUID sender;
    private final UUID receiver;

    public HyriPartyInvitation(UUID partyId, UUID sender, UUID receiver) {
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

    public static class Packet extends HyriPacket {

        private final HyriPartyInvitation invitation;

        public Packet(HyriPartyInvitation invitation) {
            this.invitation = invitation;
        }

        public HyriPartyInvitation getInvitation() {
            return this.invitation;
        }

    }

}
