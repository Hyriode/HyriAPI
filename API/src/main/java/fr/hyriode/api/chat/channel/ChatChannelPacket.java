package fr.hyriode.api.chat.channel;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 29/11/2022 at 19:34
 */
public class ChatChannelPacket extends HyriPacket {

    private final HyriChatChannel channel;
    private final UUID sender;
    private final String message;
    private final boolean component;

    public ChatChannelPacket(HyriChatChannel channel, UUID sender, String message, boolean component) {
        this.channel = channel;
        this.sender = sender;
        this.message = message;
        this.component = component;
    }

    public HyriChatChannel getChannel() {
        return this.channel;
    }

    public UUID getSender() {
        return this.sender;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isComponent() {
        return this.component;
    }

}
