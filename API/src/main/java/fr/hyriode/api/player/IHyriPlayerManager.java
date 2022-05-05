package fr.hyriode.api.player;

import fr.hyriode.api.player.nickname.IHyriNicknameManager;
import fr.hyriode.api.whitelist.IHyriWhitelistManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 21:59
 */
public interface IHyriPlayerManager {

    /**
     * Get player unique id from his name
     *
     * @param name Player's name
     * @param allowHydrionCheck Is Hydrion uuid fetching allowed
     * @return Player {@link UUID} or <code>null</code>
     */
    UUID getPlayerId(String name, boolean allowHydrionCheck);

    /**
     * Get player unique id from his name
     *
     * @param name Player's name
     * @return Player {@link UUID} or <code>null</code>
     */
    default UUID getPlayerId(String name) {
        return this.getPlayerId(name, true);
    }

    /**
     * Set player's id
     *
     * @param name Player's name
     * @param uuid Player's id
     */
    void setPlayerId(String name, UUID uuid);

    /**
     * Remove player's id
     *
     * @param name Player's name
     */
    void removePlayerId(String name);

    /**
     * Get a player with a given {@link UUID}
     *
     * @param uuid Player {@link UUID}
     * @return A player instance
     */
    IHyriPlayer getPlayer(UUID uuid);

    /**
     * Get a player from his name
     *
     * @param name Player's name
     * @return {@link IHyriPlayer} instance
     */
    IHyriPlayer getPlayer(String name);

    /**
     * Get a player from Redis with a given {@link UUID}
     *
     * @param uuid Player {@link UUID}
     * @return A player instance
     */
    IHyriPlayer getPlayerFromRedis(UUID uuid);

    /**
     * Get a player from Redis by giving his name
     *
     * @param name Player name
     * @return A player instance
     */
    IHyriPlayer getPlayerFromRedis(String name);

    /**
     * Get a player with a given {@link UUID} from Hydrion
     *
     * @param uuid Player {@link UUID}
     * @return A player instance
     */
    CompletableFuture<IHyriPlayer> getPlayerFromHydrion(UUID uuid);

    /**
     * Create a player with a given {@link UUID}
     *
     * @param online Set if the player is currently connected on the network
     * @param uuid Player {@link UUID}
     * @param name Player name
     * @return The created player
     */
    IHyriPlayer createPlayer(boolean online, UUID uuid, String name);

    /**
     * Send a player to Hydrion
     *
     * @param player The player to store in Hydrion
     */
    void sendPlayerToHydrion(IHyriPlayer player);

    /**
     * Send a player in Redis cache
     *
     * @param player {@link IHyriPlayer}
     */
    void sendPlayer(IHyriPlayer player);

    /**
     * Remove a player with a given {@link UUID}
     *
     * @param uuid Player {@link UUID}
     */
    void removePlayer(UUID uuid);

    /**
     * Kick a player from network with a given reason
     *
     * @param uuid Player {@link UUID}
     * @param reason Reason to display to player
     */
    void kickPlayer(UUID uuid, String reason);

    /**
     * Connect a player to a given server
     *
     * @param uuid Player {@link UUID}
     * @param server Server id
     */
    void connectPlayer(UUID uuid, String server);

    /**
     * Send a message to a player with a given {@link UUID}
     *
     * @param uuid Player {@link UUID}
     * @param message Message to send
     */
    void sendMessage(UUID uuid, String message);

    /**
     * Send a message component to a given
     *
     * @param uuid The unique id of the player
     * @param component The serialized component
     */
    void sendComponent(UUID uuid, String component);

    /**
     * Send a title to a player
     *
     * @param uuid The unique id of the player
     * @param title The title to send
     * @param subtitle The subtitle to send
     * @param fadeIn The time to take to show the screen
     * @param stay The time to take to stay on the screen
     * @param fadeOut The time to take to disappear from the screen
     */
    void sendTitle(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    /**
     * Send a title to all the players on the server
     *
     * @param title The title to send
     * @param subtitle The subtitle to send
     * @param fadeIn The time to take to show the screen
     * @param stay The time to take to stay on the screen
     * @param fadeOut The time to take to disappear from the screen
     */
    void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut);

    /**
     * Get a player ping with a given {@link UUID}
     *
     * @param uuid Player {@link UUID}
     * @return Player ping
     */
    int getPing(UUID uuid);

    /**
     * Get the prefix of a player from cache
     *
     * @param playerId The unique id of the player
     * @return His cached prefix
     */
    String getCachedPrefix(UUID playerId);

    /**
     * Get the prefix of a player (it will first check in cache before getting it from his account)
     *
     * @param playerId The unique id of the player
     * @return His prefix
     */
    String getPrefix(UUID playerId);

    /**
     * Save the prefix of a player in cache
     *
     * @param playerId The unique id of the player
     * @param prefix The prefix to save
     */
    void savePrefix(UUID playerId, String prefix);

    /**
     * Get the nickname manager
     *
     * @return The {@link IHyriNicknameManager}
     */
    IHyriNicknameManager getNicknameManager();

    /**
     * Get the whitelist manager instance
     *
     * @return The {@link IHyriWhitelistManager} instance
     */
    IHyriWhitelistManager getWhitelistManager();

}
