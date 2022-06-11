package fr.hyriode.api.leaderboard;

import java.util.Map;

/**
 * Created by AstFaster
 * on 05/06/2022 at 18:57
 */
public interface IHyriLeaderboardManager {

    /**
     * Get a leaderboard object
     *
     * @param type The type of the leaderboard
     * @param name The name of the leaderboard
     * @param scope The scope of the leaderboard
     * @return The {@linkplain IHyriLeaderboard leaderboard object}
     */
    IHyriLeaderboard getLeaderboard(String type, String name, HyriLeaderboardScope scope);

    /**
     * Remove a leaderboard
     *
     * @param type The type of the leaderboard
     * @param name The name of the leaderboard
     * @param scope The scope of the leaderboard
     */
    void removeLeaderboard(String type, String name, HyriLeaderboardScope scope);

    /**
     * Get all leaderboards with the same type and name
     *
     * @param type The type of the leaderboards
     * @param name The name of the leaderboards
     * @return A map of {@linkplain IHyriLeaderboard leaderboard} related to their {@link HyriLeaderboardScope}
     */
    Map<HyriLeaderboardScope, IHyriLeaderboard> getLeaderboards(String type, String name);

}
