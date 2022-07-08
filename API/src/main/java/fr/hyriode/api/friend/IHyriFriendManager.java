package fr.hyriode.api.friend;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:38
 */
public interface IHyriFriendManager {

    /** The channel used to send packets linked to friend system */
    String REDIS_CHANNEL = "friends";

    /**
     * Get the friends of a player
     *
     * @param playerId The unique id of the player
     * @return A list of {@link IHyriFriend}
     */
    List<IHyriFriend> getFriends(UUID playerId);

    /**
     * Load all friends of a player in a {@link IHyriFriendHandler}
     *
     * @param playerId The unique id of the player to load his friends
     * @return The generated {@link IHyriFriendHandler}
     */
    IHyriFriendHandler createHandler(UUID playerId);

    /**
     * Create the friend handler asynchronously
     *
     * @param playerId The unique id of the player to load his friends
     * @return The generated {@link IHyriFriendHandler} but as a {@link CompletableFuture}
     */
    CompletableFuture<IHyriFriendHandler> createHandlerAsync(UUID playerId);

    /**
     * Update friends  from a {@link IHyriFriendHandler}
     *
     * @param friendHandler The {@link IHyriFriendHandler} with data
     */
    void updateFriends(IHyriFriendHandler friendHandler);

    /**
     * Update friends in Redis database from a {@link IHyriFriendHandler}
     *
     * @param friendHandler The {@link IHyriFriendHandler} with data
     */
    void saveFriendsInCache(IHyriFriendHandler friendHandler);

    /**
     * Remove friends from cache
     *
     * @param playerId The {@link UUID} of the player
     */
    void removeCachedFriends(UUID playerId);

    /**
     * Send a friend's request to a given player
     *
     * @param sender The sender of the request
     * @param target The target of the request
     */
    void sendRequest(UUID sender, UUID target);

    /**
     * Remove a friend's request from a given player
     *
     * @param player The target of the request
     * @param sender The sender of the request
     */
    void removeRequest(UUID player, UUID sender);

    /**
     * Check if a player has a request from a given player
     *
     * @param player The target of the request
     * @param sender The sender of the request
     * @return <code>true</code> if the target has a request from the player
     */
    boolean hasRequest(UUID player, UUID sender);

    /**
     * Get all the requests of a player
     *
     * @param player The {@link UUID} of the player
     * @return A list of {@link HyriFriendRequest}
     */
    List<HyriFriendRequest> getRequests(UUID player);

}
