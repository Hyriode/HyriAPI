package fr.hyriode.api.player.model.modules;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.HyriFriendRequest;
import fr.hyriode.api.player.model.IHyriFriend;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 18/02/2023 at 09:34.<br>
 *
 * The module used to manage the friends of a {@linkplain IHyriPlayer player}
 */
public interface IHyriFriendsModule {

    /**
     * Send a request to a player
     *
     * @param target The {@link UUID} of the player that will receive the request
     */
    void sendRequest(@NotNull UUID target);

    /**
     * Remove the request of a player
     *
     * @param sender The supposed sender of the request
     */
    void removeRequest(@NotNull UUID sender);

    /**
     * Check whether the player has a request from someone
     *
     * @param sender The supposed sender of the request
     * @return <code>true</code> if the sender has requested to be a friend of the player
     */
    boolean hasRequest(@NotNull UUID sender);

    /**
     * Get all the requests sent to the player
     *
     * @return A list of {@link HyriFriendRequest}
     */
    @NotNull List<HyriFriendRequest> getRequests();

    /**
     * Get the {@link IHyriFriend} object from its uuid
     *
     * @param uuid The unique id of the friend
     * @return A {@link IHyriFriend}
     */
    IHyriFriend get(UUID uuid);

    /**
     * Get all the friends of the player
     *
     * @return A list of {@link IHyriFriend}
     */
    @NotNull List<IHyriFriend> getAll();

    /**
     * Add a friend to the player
     *
     * @param uuid The {@link UUID} of the friend to add
     */
    void add(@NotNull UUID uuid);

    /**
     * Remove a friend from the player
     *
     * @param uuid The {@link UUID} of the friend to remove
     */
    void remove(@NotNull UUID uuid);

    /**
     * Check if a player is a friend of the player
     *
     * @param uuid The {@link UUID} of the player to check
     * @return <code>true</code> if they are friends
     */
    boolean has(@NotNull UUID uuid);

}
