package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

public class BroadcastMessagePacket extends HyriPacket {

    private final String message;
    private final boolean component;

    public BroadcastMessagePacket(String message, boolean component) {
        this.message = message;
        this.component = component;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isComponent() {
        return this.component;
    }

}
