package fr.hyriode.api.player.model.modules;

import org.jetbrains.annotations.NotNull;

/**
 * Created by AstFaster
 * on 08/01/2023 at 12:51.<br>
 *
 * Represents the data used to authenticate a given player.<br>
 * Some methods only works for non-premium users.
 */
public interface IHyriAuthModule {

    /**
     * Check whether the player is a premium user
     *
     * @return <code>true</code> if the player is premium
     */
    boolean isPremium();

    /**
     * Set whether the player is a premium user
     *
     * @param premium <code>true</code> if the player is premium
     */
    void setPremium(boolean premium);

    /**
     * Get the hash that has been generated for the player's password.<br>
     * Only works if the player is not premium.
     *
     * @return A BCrypt's hash
     */
    String getHash();

    /**
     * Generate a new BCrypt hash for a given password
     *
     * @param password The password to encrypt
     */
    void newHash(@NotNull String password);

    /**
     * Check if a given password is valid.<br>
     * Only works if the player is not premium.
     *
     * @param password The password to authenticate
     * @return <code>true</code> if the password is authenticated
     */
    boolean authenticate(@NotNull String password);

}
