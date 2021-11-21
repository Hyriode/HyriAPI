package fr.hyriode.hyriapi.player;

import fr.hyriode.hyriapi.rank.HyriPermission;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 21:59
 */
public interface IHyriPlayerManager {

    /**
     * Get a player with a given {@link UUID}
     *
     * @param uuid - Player {@link UUID}
     * @return - A player instance
     */
    IHyriPlayer getPlayer(UUID uuid);

    /**
     * Create a player with a given {@link UUID}
     *
     * @param uuid - Player {@link UUID}
     * @param name - Player name
     * @return - The created player
     */
    IHyriPlayer createPlayer(UUID uuid, String name);

    /**
     * Send a player in Redis cache
     *
     * @param player - {@link IHyriPlayer}
     */
    void sendPlayer(IHyriPlayer player);

    /**
     * Remove a player with a given {@link UUID}
     *
     * @param uuid - Player {@link UUID}
     */
    void removePlayer(UUID uuid);

    /**
     * Kick a player from network with a give reason
     *
     * @param uuid - Player {@link UUID}
     * @param reason - Reason to display to player
     */
    void kickPlayer(UUID uuid, String reason);

    /**
     * Connect a player to a given server
     *
     * @param uuid - Player {@link UUID}
     * @param server - Server id
     */
    void connectPlayer(UUID uuid, String server);

    /**
     * Send a message to a player with a given {@link UUID}
     *
     * @param uuid - Player {@link UUID}
     * @param message - Message to send
     */
    void sendMessage(UUID uuid, String message);


    /**
     * Get a player ping with a given {@link UUID}
     *
     * @param uuid - Player {@link UUID}
     * @return - Player ping
     */
    int getPing(UUID uuid);

    /**
     * Check if a player has a given permission
     *
     * @param uuid Player {@link UUID}
     * @param permission Permission to check
     * @return <code>true</code> if yes
     */
    boolean hasPermission(UUID uuid, HyriPermission permission);

}
