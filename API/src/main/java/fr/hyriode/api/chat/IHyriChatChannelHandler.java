package fr.hyriode.api.chat;

import fr.hyriode.api.rank.EHyriRank;

import java.util.UUID;

/**
 * Represents a chat handler.
 */
public interface IHyriChatChannelHandler {

    /**
     * Get the channel name.
     * @return The channel name.
     */
    String getChannel();

    /**
     * Get the required rank to send a message on the channel.
     * @return The required rank.
     */
    EHyriRank getRequiredRank();

    /**
     * Get if the channel is received across the network.
     * @return <code>true</code> if the channel is received across the network.
     */
    boolean isAcrossNetwork();

    /**
     * Get if the channel can be joined by a player.
     * @return <code>true</code> if the channel can be joined by a player.
     */
    boolean canBeJoined();

    /**
     * Get the channel prefix in chat.
     * @return The channel prefix.
     */
    String getPrefix();

    /**
     * Get the message format, after the prefix.
     * @return The message format.
     */
    String getMessageFormat();

    /**
     * Triggered when a message is sent to the channel.
     * /!\ The sender should be the sender UUID, or a plugin name if the message is sent by the console or a plugin !
     * @param channel The channel name.
     * @param message The message.
     * @param sender The sender.
     * @param force <code>true</code> to bypass checks.
     */
    void onMessage(String channel, String message, UUID sender, boolean force);

    /**
     * Triggered when a message is sent to the channel, to a specific player.
     * /!\ The sender should be the sender UUID, or a plugin name if the message is sent by the console or a plugin !
     * @param channel The channel name.
     * @param receiver The receiver.
     * @param message The message.
     * @param sender The sender.
     * @param force <code>true</code> to bypass checks.
     */
    void onMessageToPlayer(String channel, UUID receiver, String message, UUID sender, boolean force);
}
