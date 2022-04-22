package fr.hyriode.api.server.join;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:28
 */
public interface IHyriJoinManager {

    void registerHandler(int priority, IHyriJoinHandler handler);

    List<UUID> getExpectedPlayers();

    void addExpectedPlayer(UUID playerId);

    void removeExpectedPlayer(UUID playerId);

    List<UUID> getExpectedModerators();

    void addExpectedModerator(UUID playerId);

    void removeExpectedModerator(UUID playerId);

}
