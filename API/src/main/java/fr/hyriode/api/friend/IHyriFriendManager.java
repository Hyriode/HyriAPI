package fr.hyriode.api.friend;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:38
 */
public interface IHyriFriendManager {

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
    IHyriFriendHandler loadFriends(UUID playerId);

    /**
     * Update friends in database
     */
    void updateFriends(IHyriFriendHandler friendHandler);

}
