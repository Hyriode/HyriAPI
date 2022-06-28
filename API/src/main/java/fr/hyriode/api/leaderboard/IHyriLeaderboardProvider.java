package fr.hyriode.api.leaderboard;

/**
 * Created by AstFaster
 * on 28/06/2022 at 13:56
 */
public interface IHyriLeaderboardProvider {

    /**
     * Get a leaderboard object
     *
     * @param type The type of the leaderboard
     * @param name The name of the leaderboard
     * @return The {@linkplain IHyriLeaderboard leaderboard}
     */
    IHyriLeaderboard getLeaderboard(String type, String name);

}
