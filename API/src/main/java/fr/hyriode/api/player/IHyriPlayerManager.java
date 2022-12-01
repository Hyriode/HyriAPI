package fr.hyriode.api.player;

import fr.hyriode.api.player.nickname.IHyriNicknameManager;
import fr.hyriode.api.whitelist.IHyriWhitelistManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

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
     * @return Player {@link UUID} or <code>null</code>
     */
    UUID getPlayerId(String name);

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
     * Create a player with a given {@link UUID}
     *
     * @param premium Set whether the player is a premium player
     * @param uuid Player {@link UUID}
     * @param name Player name
     * @return The created player
     */
    IHyriPlayer createPlayer(boolean premium, UUID uuid, String name);

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
    default IHyriPlayer getPlayer(String name) {
        final UUID playerId = this.getPlayerId(name);

        return playerId == null ? null : this.getPlayer(playerId);
    }

    /**
     * Update a player's account
     *
     * @param player The {@link IHyriPlayer} object
     */
    void updatePlayer(IHyriPlayer player);

    /**
     * Remove a player with a given {@link UUID}
     *
     * @param uuid The {@link UUID} of the player
     */
    void removePlayer(UUID uuid);

    /**
     * Get all registered players on the network.<br>
     * Warning: This method is extremely costly in performance. Indeed, it will query the database for each player account and deserialize them.
     *
     * @return A list of {@link IHyriPlayer}
     */
    List<IHyriPlayer> getPlayers();

    /**
     * Get all registered players' {@link UUID} on the network.<br>
     * Warning: This method is extremely costly in performance. Indeed, it will query the database for each player {@link UUID}.
     *
     * @return A list of {@link UUID}
     */
    List<UUID> getPlayersId();

    /**
     * Check whether a player is online or not.
     *
     * @param playerId The {@link UUID} of the player
     * @return <code>true</code> if the player is online
     */
    default boolean isOnline(UUID playerId) {
        return this.getSession(playerId) != null;
    }

    /**
     * Get a player session from cache.
     *
     * @param playerId The {@link UUID} of the player
     * @return The found {@link IHyriPlayerSession}
     */
    @Nullable IHyriPlayerSession getSession(UUID playerId);

    /**
     * Update a player session in cache
     *
     * @param session The {@link IHyriPlayerSession} to update
     */
    void updateSession(@NotNull IHyriPlayerSession session);

    /**
     * Delete the session of a given player
     *
     * @param playerId The {@link UUID} of the player
     */
    void deleteSession(@NotNull UUID playerId);

    /**
     * Get all the registered player sessions on the network
     * Warning: This method is extremely costly in performance. Indeed, it will query the database for each player session and deserialize them.
     *
     * @return A list of {@link IHyriPlayerSession}
     */
    List<IHyriPlayerSession> getSessions();

    /**
     * Kick a player from network with a given reason
     *
     * @param uuid Player {@link UUID}
     * @param component The serialized text component to show to the player
     */
    void kickPlayer(UUID uuid, String component);

    /**
     * Connect a player to a given server
     *
     * @param uuid Player {@link UUID}
     * @param server Server id
     */
    void connectPlayer(UUID uuid, String server);

    /**
     * Broadcast a message on all servers
     *
     * @param message The message to broadcast
     * @param component Is the message a serialized TextComponent or a simple {@link String}
     */
    void broadcastMessage(String message, boolean component);

    /**
     * Broadcast a message on all servers
     *
     * @param message The message to broadcast
     */
    default void broadcastMessage(String message) {
        this.broadcastMessage(message, false);
    }

    /**
     * Send a message to a player
     *
     * @param uuid Player {@link UUID}
     * @param message Message to send
     * @param component Is the message a serialized TextComponent or a simple {@link String}
     */
    void sendMessage(UUID uuid, String message, boolean component);

    /**
     * Send a message to a player
     *
     * @param uuid Player {@link UUID}
     * @param message Message to send
     */
    default void sendMessage(UUID uuid, String message) {
        this.sendMessage(uuid, message, false);
    }

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
