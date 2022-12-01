package fr.hyriode.api.chat.channel;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Manager for chat channels.
 */
public interface IHyriChatChannelManager {

    /**
     * Register a handler of a given channel
     *
     * @param channel The channel handled by the {@link IHyriChatChannelHandler}
     * @param handler The handler register to his channel.
     */
    void registerHandler(@NotNull HyriChatChannel channel, @NotNull IHyriChatChannelHandler handler);

    /**
     * Unregister the current handler of a given channel
     *
     * @param channel The channel
     */
    void unregisterHandler(@NotNull HyriChatChannel channel);

    /**
     * Get the handler for a channel.
     *
     * @param channel The channel to use.
     * @return The handlers for the channel.
     */
    IHyriChatChannelHandler getHandler(@NotNull HyriChatChannel channel);

    /**
     * Send a message on a given channel
     *
     * @param channel The channel where the message will be sent
     * @param sender The {@link UUID} of the sender
     * @param message The message to send
     * @param component Is the message a TextComponent or a simple {@link String}
     */
    void sendMessage(@NotNull HyriChatChannel channel, @NotNull UUID sender, @NotNull String message, boolean component);

}
