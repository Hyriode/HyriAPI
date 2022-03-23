package fr.hyriode.api.friend;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:12
 */
public interface IHyriFriendHandler {

    /**
     * Get the unique id of the owner of the handler
     *
     * @return An {@link UUID}
     */
    UUID getOwner();

    /**
     * Get the {@link IHyriFriend} object from its uuid
     *
     * @param uuid The unique id of the friend
     * @return A {@link IHyriFriend}
     */
    IHyriFriend getFriend(UUID uuid);

    /**
     * Get all the friends that have the player
     *
     * @return A list of {@link IHyriFriend}
     */
    List<IHyriFriend> getFriends();

    /**
     * Get unique ids of all player's friends
     *
     * @return A list of {@link UUID}
     */
    List<UUID> getFriendsIds();

    /**
     * Add a friend to the player
     *
     * @param friend The {@link IHyriFriend} to add
     */
    void addFriend(IHyriFriend friend);

    /**
     * Add a friend to the player
     *
     * @param uuid The unique id of the friend to add
     */
    void addFriend(UUID uuid);

    /**
     * Remove a friend from the player
     *
     * @param friend The {@link IHyriFriend} to remove
     */
    void removeFriend(IHyriFriend friend);

    /**
     * Remove a friend from the player
     *
     * @param uuid The unique id of the friend to remove
     */
    void removeFriend(UUID uuid);

    /**
     * Check if a player is a friend of the player
     *
     * @param uuid Player's uuid to check
     * @return <code>true</code> if they are friends
     */
    boolean areFriends(UUID uuid);

    /**
     * Update the friends.<br>
     * Use this method after doing an action on the player's friends
     */
    void update();

}
