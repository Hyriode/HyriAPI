package fr.hyriode.api.leaderboard;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/06/2022 at 18:53
 */
public interface IHyriLeaderboard {

    /**
     * Get the type of the leaderboard
     *
     * @return A type
     */
    String getType();

    /**
     * Get the name of the leaderboard
     *
     * @return A name
     */
    String getName();

    /**
     * Get the scope of the leaderboard
     *
     * @return A {@link HyriLeaderboardScope}
     */
    HyriLeaderboardScope getScope();

    /**
     * Get the leaders of the leaderboard (0: first, 1: second, etc)
     *
     * @return A list of {@link UUID}
     */
    List<UUID> getLeaders();

    /**
     * Get the leader of the leaderboard in a custom range
     *
     * @param start The start of the range
     * @param stop The stop of the range
     * @return A list of {@link UUID}
     */
    List<UUID> getLeaders(long start, long stop);

    /**
     * Get all the scores registered in the leaderboard
     *
     * @return A map of score related to their owner
     */
    Map<UUID, Integer> getScores();

    /**
     * Get all scores in a custom range
     *
     * @param start The start of the range
     * @param stop The stop of the range
     * @return A map of score related to their owner
     */
    Map<UUID, Integer> getScores(long start, long stop);

    /**
     * Get a score by its identifier
     *
     * @param id The identifier of the score
     * @return The score
     */
    int getScore(UUID id);

    /**
     * Set the score related to an identifier
     *
     * @param id The identifier
     * @param score The score
     */
    void setScore(UUID id, int score);

    /**
     * Increment an existing score
     *
     * @param id The identifier
     * @param score The score to add
     */
    void incrementScore(UUID id, int score);

    /**
     * Remove a score
     *
     * @param id The identifier of the score
     */
    void removeScore(UUID id);

}
