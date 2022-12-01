package fr.hyriode.api.whitelist;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/04/2022 at 07:05
 */
public interface IHyriWhitelistManager {

    /**
     * Whitelist a player on the network
     *
     * @param name The name of the player to whitelist
     * @param seconds The ttl of the whitelist
     */
    void whitelistPlayer(String name, int seconds);

    /**
     * Whitelist a player on the network
     *
     * @param name The name of the player to whitelist
     */
    default void whitelistPlayer(String name) {
        this.whitelistPlayer(name, 2 * 60);
    }

    /**
     * Remove a player from the whitelist
     *
     * @param name The name of the player to remove
     */
    void removePlayerFromWhitelist(String name);

    /**
     * Check whether a player is whitelisted or not
     *
     * @param name The name of the player
     * @return <code>true</code> if he is whitelisted
     */
    boolean isWhitelisted(String name);

}
