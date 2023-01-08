package fr.hyriode.api.player.auth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 08/01/2023 at 12:51
 */
public interface IHyriAuthManager {

    /**
     * Generate a new auth object for a player
     *
     * @param playerId The {@link UUID} of the player
     * @param password The password of the user
     * @return The new {@link IHyriAuth}
     */
    @NotNull IHyriAuth newAuth(@NotNull UUID playerId, @NotNull String password);

    /**
     * Delete an auth of a player
     *
     * @param playerId The {@link UUID} of the player
     */
    void deleteAuth(@NotNull UUID playerId);

    /**
     * Get the auth of a player
     *
     * @param playerId The {@link UUID} of the player
     * @return The {@link IHyriAuth} linked to the player
     */
    @Nullable IHyriAuth getAuth(@NotNull UUID playerId);

    /**
     * Check whether a player has auth credentials stored
     *
     * @param playerId The {@link UUID} of the player
     */
    boolean hasAuth(@NotNull UUID playerId);

}
