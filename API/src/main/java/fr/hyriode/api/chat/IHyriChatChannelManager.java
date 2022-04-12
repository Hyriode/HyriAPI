package fr.hyriode.api.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Manager for chat channels.
 */
public interface IHyriChatChannelManager {

    /**
     * Get the handler for a channel.
     * @param channel The channel to use.
     * @return The handlers for the channel.
     */
    IHyriChatChannelHandler getHandler(String channel);

    /**
     * Register a channel.
     * @param handler The handler register to his channel.
     */
    void registerChannel(IHyriChatChannelHandler handler);

    /**
     * Unregister a channel.
     * @param channel The channel to unregister.
     */
    void unregisterChannel(String channel);

    /**
     * Send a message through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     */
    default void sendMessage(String channel, String message) {
        this.sendMessage(channel, message, false);
    }

    /**
     * Send a message through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param force <code>true</code> to bypass checks.
     */
    default void sendMessage(String channel, String message, boolean force) {
        this.sendMessage(channel, message, null, force);
    }

    /**
     * Send a message through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param sender The sender of the message.
     */
    default void sendMessage(String channel, String message, UUID sender) {
        this.sendMessage(channel, message, sender, false);
    }

    /**
     * Send a message through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param sender The sender of the message.
     * @param force <code>true</code> to bypass checks.
     */
    void sendMessage(String channel, String message, UUID sender, boolean force);

    /**
     * Send a message to a player through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param player The player to send the message to.
     */
    default void sendMessageToPlayer(String channel, String message, UUID player) {
        this.sendMessageToPlayer(channel, message, player, false);
    }

    /**
     * Send a message to a player through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param player The player to send the message to.
     * @param force <code>true</code> to bypass checks.
     */
    default void sendMessageToPlayer(String channel, String message, UUID player, boolean force) {
        this.sendMessageToPlayer(channel, message, player, null, force);
    }

    /**
     * Send a message to a player through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param player The player to send the message to.
     * @param sender The sender of the message.
     */
    default void sendMessageToPlayer(String channel, String message, UUID player, UUID sender) {
        this.sendMessageToPlayer(channel, message, player, sender, false);
    }

    /**
     * Send a message to a player through a channel.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param player The player to send the message to.
     * @param sender The sender of the message.
     * @param force <code>true</code> to bypass checks.
     */
    void sendMessageToPlayer(String channel, String message, UUID player, UUID sender, boolean force);

    /**
     * <code>true</code> if the player can access to the channel.
     * @param channel The channel to use.
     * @param player The player to check.
     * @return <code>true</code> if the player can use the channel.
     */
    static boolean canPlayerAccessChannel(String channel, IHyriPlayer player) {
        return HyriAPI.get().getChatChannelManager().getHandler(channel).getRequiredRank().getId() <= player.getRank().getType().getId();
    }
}
