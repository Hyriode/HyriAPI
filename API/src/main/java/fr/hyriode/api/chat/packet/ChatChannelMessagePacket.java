package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class ChatChannelMessagePacket extends HyriPacket {

    private final String channel;
    private final String message;
    private final UUID sender;
    private final boolean force;

    public ChatChannelMessagePacket(String channel, String message, UUID sender, boolean force) {
        this.channel = channel;
        this.message = message;
        this.sender = sender;
        this.force = force;
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

    public boolean isForce() {
        return this.force;
    }
}
