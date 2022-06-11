package fr.hyriode.api.impl.common.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.HyriLeaderboardScope;
import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import redis.clients.jedis.resps.Tuple;

import java.util.*;

/**
 * Created by AstFaster
 * on 05/06/2022 at 20:16
 */
public class HyriLeaderboard implements IHyriLeaderboard {

    private final String key;

    private final String type;
    private final String name;
    private final HyriLeaderboardScope scope;

    public HyriLeaderboard(String key, String type, String name, HyriLeaderboardScope scope) {
        this.key = key;
        this.type = type;
        this.name = name;
        this.scope = scope;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public HyriLeaderboardScope getScope() {
        return this.scope;
    }

    @Override
    public List<UUID> getLeaders() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<UUID> leaders = new ArrayList<>();

            for (String member : jedis.zrange(this.key, 0, -1)) {
                leaders.add(UUID.fromString(member));
            }
            return leaders;
        });
    }

    @Override
    public Map<UUID, Integer> getScores() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Map<UUID, Integer> scores = new HashMap<>();
            final List<Tuple> tuples = jedis.zrangeWithScores(this.key, 0, -1);

            for (Tuple tuple : tuples) {
                scores.put(UUID.fromString(tuple.getElement()), (int) tuple.getScore());
            }
            return scores;
        });
    }

    @Override
    public int getScore(UUID id) {
        return (int) (double) (HyriAPI.get().getRedisProcessor().get(jedis -> jedis.zscore(this.key, id.toString())));
    }

    @Override
    public void setScore(UUID id, int score) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final String idStr = id.toString();

            jedis.zrem(this.key, idStr);
            jedis.zadd(this.key, score, idStr);
        });
    }

    @Override
    public void incrementScore(UUID id, int score) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.zadd(this.key, score, id.toString()));
    }

    @Override
    public void removeScore(UUID id) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.zrem(this.key, id.toString()));
    }

}
