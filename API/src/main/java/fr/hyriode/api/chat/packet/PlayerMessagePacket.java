package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class PlayerMessagePacket extends HyriPacket {

    private final UUID player;
    private final String message;

    public PlayerMessagePacket(UUID player, String message) {
        this.player = player;
        this.message = message;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public String getMessage() {
        return this.message;
    }

}
