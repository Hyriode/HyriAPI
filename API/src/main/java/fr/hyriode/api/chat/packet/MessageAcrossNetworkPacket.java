package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class MessageAcrossNetworkPacket extends HyriPacket {

    private final String channel;
    private final String message;
    private final UUID sender;

    public MessageAcrossNetworkPacket(String channel, String message) {
        this.channel = channel;
        this.message = message;
        this.sender = null;
    }

    public MessageAcrossNetworkPacket(String channel, String message, UUID sender) {
        this.channel = channel;
        this.message = message;
        this.sender = sender;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }

    public UUID getSender() {
        return this.sender;
    }
}
