package fr.hyriode.api.impl.common.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.HyriLeaderboardScope;
import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import fr.hyriode.api.leaderboard.IHyriLeaderboardManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AstFaster
 * on 05/06/2022 at 20:14
 */
public class HyriLeaderboardManager implements IHyriLeaderboardManager {

    @Override
    public IHyriLeaderboard getLeaderboard(String type, String name, HyriLeaderboardScope scope) {
        return new HyriLeaderboard(this.createKey(type, name, scope), type, name, scope);
    }

    @Override
    public void removeLeaderboard(String type, String name, HyriLeaderboardScope scope) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.zremrangeByScore(this.createKey(type, name, scope), 0, -1));
    }

    @Override
    public Map<HyriLeaderboardScope, IHyriLeaderboard> getLeaderboards(String type, String name) {
        final Map<HyriLeaderboardScope, IHyriLeaderboard> leaderboards = new HashMap<>();

        for (HyriLeaderboardScope scope : HyriLeaderboardScope.values()) {
            if (HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(this.createKey(type, name, scope)))) {
                leaderboards.put(scope, this.getLeaderboard(type, name, scope));
            }
        }
        return leaderboards;
    }

    private String createKey(String type, String name, HyriLeaderboardScope scope) {
        return "leaderboards:" + type + ":" + name + ":" + scope.getId();
    }

}
