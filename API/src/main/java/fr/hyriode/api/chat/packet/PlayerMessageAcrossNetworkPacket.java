package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class PlayerMessageAcrossNetworkPacket extends HyriPacket {

    private final UUID player;
    private final String channel;
    private final String message;
    private final UUID sender;

    public PlayerMessageAcrossNetworkPacket(UUID player, String channel, String message) {
        this.player = player;
        this.channel = channel;
        this.message = message;
        this.sender = null;
    }

    public PlayerMessageAcrossNetworkPacket(UUID player, String channel, String message, UUID sender) {
        this.player = player;
        this.channel = channel;
        this.message = message;
        this.sender = sender;
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
}
