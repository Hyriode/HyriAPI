package fr.hyriode.api.server.join;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:28
 */
public interface IHyriJoinHandler {

    default HyriJoinResponse requestJoin(UUID player, HyriJoinResponse currentResponse) {
        return currentResponse;
    }

    default String createResponseMessage(UUID playerId, HyriJoinResponse response) {
        return null;
    }

    default void onLogin(UUID player, String name) {}

    default void onJoin(UUID player) {}

    default void onLogout(UUID player) {}

    /**
     * Check whether a player is expected to connect or no
     *
     * @param playerId The {@link UUID} of the player
     * @return <code>true</code> if he is expected to join
     */
    boolean isExpected(UUID playerId);

}
