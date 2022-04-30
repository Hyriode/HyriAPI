package fr.hyriode.api.chat.channel;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;

import java.util.UUID;

/**
 * Manager for chat channels.
 */
public interface IHyriChatChannelManager {

    /**
     * Get the handler for a channel.
     *
     * @param channel The channel to use.
     * @return The handlers for the channel.
     */
    IHyriChatChannelHandler getHandler(String channel);

    /**
     * Register a channel.
     *
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
     * @param sender The sender of the message.
     * @param force <code>true</code> to bypass checks.
     */
    void sendMessage(String channel, String message, UUID sender, boolean force);

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
     * Send a given text component on a channel
     *
     * @param channel The channel to use
     * @param component The component to send
     * @param force <code>true</code> to bypass checks
     */
    void sendComponent(String channel, String component, boolean force);

    /**
     * Send a given text component on a channel
     *
     * @param channel The channel to use
     * @param component The component to send
     */
    default void sendComponent(String channel, String component) {
        this.sendComponent(channel, component, false);
    }

    /**
     * <code>true</code> if the player can access to the channel.
     * @param channel The channel to use.
     * @param player The player to check.
     * @return <code>true</code> if the player can use the channel.
     */
    static boolean canPlayerAccessChannel(String channel, IHyriPlayer player) {
        final HyriRank rank = player.getRank();
        final IHyriChatChannelHandler handler = HyriAPI.get().getChatChannelManager().getHandler(channel);
        final HyriPlayerRankType playerRankType = handler.getRequiredPlayerRank();
        final HyriStaffRankType staffRankType = handler.getRequiredStaffRank();

        if (playerRankType == null && staffRankType == null) {
            return true;
        }

        boolean result = playerRankType != null && rank.isSuperior(playerRankType);

        if (staffRankType != null && rank.isSuperior(staffRankType)) {
            result = true;
        } else if (staffRankType != null){
            result = false;
        }
        return result;
    }

}
