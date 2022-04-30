package fr.hyriode.api.chat.channel;

import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;

import java.util.UUID;

/**
 * Represents a chat handler.
 */
public interface IHyriChatChannelHandler {

    /**
     * Get the channel name.
     *
     * @return The channel name.
     */
    String getChannel();

    /**
     * Get the required staff rank to send a message on the channel.
     * @return The required rank.
     */
    HyriStaffRankType getRequiredStaffRank();

    /**
     * Get the required player rank to send a message on the channel.
     * @return The required rank.
     */
    HyriPlayerRankType getRequiredPlayerRank();

    /**
     * Check if messages sent on the channel need to be sent across network
     *
     * @return <code>true</code> if yes
     */
    boolean isAcrossNetwork();

    /**
     * Triggered when a message is received on the channel.<br>
     * /!\ The sender should be the sender UUID, or a plugin name if the message is sent by the console or a plugin!
     *
     * @param channel The channel name.
     * @param message The message.
     * @param sender The sender.
     * @param force <code>true</code> to bypass checks.
     */
    void onMessage(String channel, String message, UUID sender, boolean force);

    /**
     * Triggered when a component is received on the channel.<br>
     *
     * @param channel The channel name
     * @param component The received component
     * @param force <code>true</code> to bypass checks
     */
    void onComponent(String channel, String component, boolean force);

}
