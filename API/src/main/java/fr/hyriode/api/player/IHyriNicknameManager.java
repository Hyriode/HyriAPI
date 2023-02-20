package fr.hyriode.api.player;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 21:58.<br>
 *
 * The manager of nicknames.
 */
public interface IHyriNicknameManager {

    /**
     * Check whether a nickname is available or not
     *
     * @param name The name to check
     * @return <code>true</code> if the nickname is available
     */
    boolean isNicknameAvailable(String name);

    /**
     * Set a nickname as used
     *
     * @param name The name to set as used
     * @param player The {@link UUID} of the player using the nickname
     */
    void addUsedNickname(String name, UUID player);

    /**
     * Set a nickname as not-used
     *
     * @param name The name to set as un-used
     */
    void removeUsedNickname(String name);

    /**
     * Get the player using a given nickname
     *
     * @param name The nickname to check
     * @return The {@link UUID} of the player (it he exists)
     */
    UUID getPlayerUsingNickname(String name);

}
