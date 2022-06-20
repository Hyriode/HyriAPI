package fr.hyriode.api.leaderboard;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 16/06/2022 at 20:02
 */
public class HyriLeaderboardScore {

    private final UUID id;
    private final long value;

    public HyriLeaderboardScore(UUID id, long value) {
        this.id = id;
        this.value = value;
    }

    public UUID getId() {
        return this.id;
    }

    public long getValue() {
        return this.value;
    }

}
