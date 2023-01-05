package fr.hyriode.api.leaderboard;

import java.util.List;
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
     * Get the leaders of the leaderboard (0: first, 1: second, etc)
     *
     * @param scope The scope to use
     * @return A list of {@link UUID}
     */
    List<UUID> getLeaders(HyriLeaderboardScope scope);

    /**
     * Get the leader of the leaderboard in a custom range
     *
     * @param scope The scope to use
     * @param start The start of the range
     * @param stop The stop of the range
     * @return A list of {@link UUID}
     */
    List<UUID> getLeaders(HyriLeaderboardScope scope, long start, long stop);

    /**
     * Get the position of a given identifier
     *
     * @param scope The scope to use
     * @param id The identifier
     * @return The position of the identifier
     */
    long getPosition(HyriLeaderboardScope scope, UUID id);

    /**
     * Get all the scores registered in the leaderboard
     *
     * @param scope The scope to use
     * @return A list of score related to their owner
     */
    List<HyriLeaderboardScore> getScores(HyriLeaderboardScope scope);

    /**
     * Get all scores in a custom range
     *
     * @param scope The scope to use
     * @param start The start of the range
     * @param stop The stop of the range
     * @return A list of score related to their owner
     */
    List<HyriLeaderboardScore> getScores(HyriLeaderboardScope scope, long start, long stop);

    /**
     * Get a score by its identifier
     *
     * @param scope The scope to use
     * @param id The identifier of the score
     * @return The score
     */
    double getScore(HyriLeaderboardScope scope, UUID id);

    /**
     * Set the score related to an identifier
     *
     * @param scope The scope to use
     * @param id The identifier
     * @param score The score
     */
    void setScore(HyriLeaderboardScope scope, UUID id, double score);

    /**
     * Set the score related to an identifier
     *
     * @param id The identifier
     * @param score The score
     */
    default void setScore(UUID id, double score) {
        for (HyriLeaderboardScope scope : HyriLeaderboardScope.values()) {
            this.setScore(scope, id, score);
        }
    }

    /**
     * Increment an existing score
     *
     * @param scope The scope to use
     * @param id The identifier
     * @param score The score to add
     */
    void incrementScore(HyriLeaderboardScope scope, UUID id, double score);

    /**
     * Increment an existing score
     *
     * @param id The identifier
     * @param score The score to add
     */
    default void incrementScore(UUID id, double score) {
        for (HyriLeaderboardScope scope : HyriLeaderboardScope.values()) {
            this.incrementScore(scope, id, score);
        }
    }

    /**
     * Remove a score
     *
     * @param scope The scope to use
     * @param id The identifier of the score
     */
    void removeScore(HyriLeaderboardScope scope, UUID id);

    /**
     * Get the total size of a leaderbaord with a precise scope
     *
     * @param scope The scope to use
     * @return The size of the leaderboard
     */
    long getSize(HyriLeaderboardScope scope);

    /**
     * Clear all the scores of a given scope
     *
     * @param scope The scope to use
     */
    void clear(HyriLeaderboardScope scope);

    /**
     * Clear all the scores of the scoreboard
     */
    default void clear() {
        for (HyriLeaderboardScope scope : HyriLeaderboardScope.values()) {
            this.clear(scope);
        }
    }

}
