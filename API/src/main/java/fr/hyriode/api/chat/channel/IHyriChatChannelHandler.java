package fr.hyriode.api.chat.channel;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 29/11/2022 at 19:27.<br>
 *
 * The handler linked to a {@link HyriChatChannel}.
 */
@FunctionalInterface
public interface IHyriChatChannelHandler {

    /**
     * Triggered when a message is received on the {@link HyriChatChannel}
     *
     * @param sender The sender of the message
     * @param message The received message
     * @param component Is the message a TextComponent or a simple {@link String}
     */
    void onMessage(UUID sender, String message, boolean component);

}
