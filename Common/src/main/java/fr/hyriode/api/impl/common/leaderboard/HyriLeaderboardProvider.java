package fr.hyriode.api.impl.common.leaderboard;

import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;

/**
 * Created by AstFaster
 * on 05/06/2022 at 20:14
 */
public class HyriLeaderboardProvider implements IHyriLeaderboardProvider {

    @Override
    public IHyriLeaderboard getLeaderboard(String type, String name) {
        return new HyriLeaderboard(type, name);
    }

}
