package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class PlayerMessagePacket extends HyriPacket {

    private final UUID player;
    private final String message;
    private final boolean component;

    public PlayerMessagePacket(UUID player, String message, boolean component) {
        this.player = player;
        this.message = message;
        this.component = component;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isComponent() {
        return this.component;
    }

}
