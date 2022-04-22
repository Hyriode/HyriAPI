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

    default HyriJoinResponse requestPartyJoin(UUID partyId, HyriJoinResponse currentResponse) {
        return currentResponse;
    }

    default String createResponseMessage(UUID playerId, HyriJoinResponse response) {
        return null;
    }

    default void onLogin(UUID player, String name) {}

    default void onPlayerJoin(UUID player) {}

    default void onModeratorJoin(UUID moderator) {}

    default void onLogout(UUID player) {}

}
