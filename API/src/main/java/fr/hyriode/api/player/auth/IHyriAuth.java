package fr.hyriode.api.player.auth;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 08/01/2023 at 12:51.<br>
 *
 * Represents the data used to authenticate a given player (more precisely, a crack-player).
 */
public interface IHyriAuth {

    /**
     * Get the {@link UUID} of the player
     *
     * @return A {@link UUID}
     */
    @NotNull UUID getPlayerId();

    /**
     * Get the hash that has been generated for the player's password
     *
     * @return A BCrypt's hash
     */
    @NotNull String getHash();

    /**
     * Authenticate a password
     *
     * @param password The password to authenticate
     * @return <code>true</code> if the password is authenticated
     */
    boolean authenticate(@NotNull String password);

}
