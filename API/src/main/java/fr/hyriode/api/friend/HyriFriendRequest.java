package fr.hyriode.api.friend;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class HyriFriendRequest {

    private final UUID sender;
    private final UUID receiver;

    public HyriFriendRequest(UUID sender, UUID receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID getReceiver() {
        return this.receiver;
    }

    public static class Packet extends HyriPacket {

        private final HyriFriendRequest request;

        public Packet(HyriFriendRequest request) {
            this.request = request;
        }

        public HyriFriendRequest getRequest() {
            return this.request;
        }

    }

}
