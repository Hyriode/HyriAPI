package fr.hyriode.api.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Manager for chat channels.
 */
public interface IHyriChatManager {

    String PUBSUB_CHANNEL = "chat";

    /**
     * Get the handler for a channel.
     * @param channel The channel to use.
     * @return The handlers for the channel.
     */
    IHyriChatHandler getHandler(String channel);

    /**
     * Register a channel.
     * @param channel The channel to register.
     * @param handler The handler to use for this channel.
     */
    void registerChannel(String channel, IHyriChatHandler handler);

    /**
     * Unregister a channel.
     * @param channel The channel to unregister.
     */
    void unregisterChannel(String channel);

    /**
     * Send a message to all players through a channel across the network.
     * @param channel The channel to use.
     * @param message The message to send.
     */
    void sendMessageAcrossNetwork(String channel, String message);

    /**
     * Send a message to all players through a channel across the network.
     * @param channel The channel to use.
     * @param message The message to send.
     * @param callback The callback to use.
     */
    void sendMessageAcrossNetwork(String channel, String message, Runnable callback);

    /**
     * Send a message to a player through a channel.
     * @param channel The channel to use.
     * @param player The player to send the message to.
     * @param message The message to send.
     */
    void sendMessageToPlayerAcrossNetwork(String channel, UUID player, String message);

    /**
     * Send a message to a player through a channel.
     * @param channel The channel to use.
     * @param player The player to send the message to.
     * @param message The message to send.
     * @param callback The callback to use.
     */
    void sendMessageToPlayerAcrossNetwork(String channel, UUID player, String message, Runnable callback);

    /**
     * <code>true</code> if the player can use the channel.
     * @param channel The channel to use.
     * @param player The player to check.
     * @return <code>true</code> if the player can use the channel.
     */
    static boolean canPlayerJoinChannel(String channel, IHyriPlayer player) {
        return HyriAPI.get().getChatManager().getHandler(channel).getRequiredRank().getId() >= player.getRank().getType().getId();
    }
}
