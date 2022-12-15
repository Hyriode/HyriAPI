package fr.hyriode.api.chat.channel;

import fr.hyriode.api.event.HyriCancellableEvent;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 07/12/2022 at 17:40.<br>
 *
 * Event triggered each time a message is sent on a chat channel.
 */
public class ChatChannelMessageEvent extends HyriCancellableEvent {

    private final HyriChatChannel channel;
    private final UUID sender;
    private final String message;
    private final boolean component;

    public ChatChannelMessageEvent(HyriChatChannel channel, UUID sender, String message, boolean component) {
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
