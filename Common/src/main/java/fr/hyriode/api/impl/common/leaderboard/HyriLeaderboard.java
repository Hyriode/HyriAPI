package fr.hyriode.api.impl.common.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.HyriLeaderboardScope;
import fr.hyriode.api.leaderboard.HyriLeaderboardScore;
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
        return this.getLeaders(0, -1);
    }

    @Override
    public List<UUID> getLeaders(long start, long stop) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<UUID> leaders = new ArrayList<>();

            for (String member : jedis.zrange(this.key, start, stop)) {
                leaders.add(UUID.fromString(member));
            }
            return leaders;
        });
    }

    @Override
    public long getPosition(UUID id) {
        try {
            return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.zrevrank(this.key, id.toString()));
        } catch (NullPointerException e) {
            return -1;
        }
    }

    @Override
    public List<HyriLeaderboardScore> getScores() {
        return this.getScores(0, -1);
    }

    @Override
    public List<HyriLeaderboardScore> getScores(long start, long stop) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<HyriLeaderboardScore> scores = new ArrayList<>();
            final List<Tuple> tuples = jedis.zrangeWithScores(this.key, start, stop);

            for (Tuple tuple : tuples) {
                scores.add(new HyriLeaderboardScore(UUID.fromString(tuple.getElement()), (long) tuple.getScore()));
            }

            Collections.reverse(scores);

            return scores;
        });
    }

    @Override
    public int getScore(UUID id) {
        try {
            return (int) (double) (HyriAPI.get().getRedisProcessor().get(jedis -> jedis.zscore(this.key, id.toString())));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void setScore(UUID id, long score) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.zadd(this.key, score, id.toString()));
    }

    @Override
    public void incrementScore(UUID id, long score) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.zincrby(this.key, score, id.toString()));
    }

    @Override
    public void removeScore(UUID id) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.zrem(this.key, id.toString()));
    }

}
