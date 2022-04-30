package fr.hyriode.api.chat.channel;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

public class ChatChannelComponentPacket extends HyriPacket {

    private final String channel;
    private final String component;
    private final boolean force;

    public ChatChannelComponentPacket(String channel, String component, boolean force) {
        this.channel = channel;
        this.component = component;
        this.force = force;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getComponent() {
        return this.component;
    }

    public boolean isForce() {
        return this.force;
    }

}
