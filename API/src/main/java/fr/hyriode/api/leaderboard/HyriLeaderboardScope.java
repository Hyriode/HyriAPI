package fr.hyriode.api.leaderboard;

/**
 * Created by AstFaster
 * on 06/06/2022 at 09:49
 */
public enum HyriLeaderboardScope {

    /** A leaderboard that never resets and counts the total of scores  */
    TOTAL("total"),
    /** A leaderboard resetting each month and counting monthly scores  */
    MONTHLY("monthly"),
    /** A leaderboard resetting each week and counting weekly scores */
    WEEKLY("weekly");

    /** The id of the scope */
    private final String id;

    /**
     * The default constructor of a {@link HyriLeaderboardScope}
     *
     * @param id The id of the scope
     */
    HyriLeaderboardScope(String id) {
        this.id = id;
    }

    /**
     * Get the identifier of the scope
     *
     * @return An identifier
     */
    public String getId() {
        return this.id;
    }

}
