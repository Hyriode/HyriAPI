package fr.hyriode.api.leaderboard;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 16/06/2022 at 20:02
 */
public class HyriLeaderboardScore {

    private final UUID id;
    private final double value;

    public HyriLeaderboardScore(UUID id, double value) {
        this.id = id;
        this.value = value;
    }

    public UUID getId() {
        return this.id;
    }

    public double getValue() {
        return this.value;
    }

}
