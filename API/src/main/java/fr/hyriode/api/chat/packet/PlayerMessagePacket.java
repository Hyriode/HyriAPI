package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class PlayerMessagePacket extends HyriPacket {

    private final UUID player;
    private final String channel;
    private final String message;
    private final UUID sender;
    private final boolean force;

    public PlayerMessagePacket(UUID player, String channel, String message, UUID sender, boolean force) {
        this.player = player;
        this.channel = channel;
        this.message = message;
        this.sender = sender;
        this.force = force;
    }

    public UUID getPlayer() {
        return this.player;
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
