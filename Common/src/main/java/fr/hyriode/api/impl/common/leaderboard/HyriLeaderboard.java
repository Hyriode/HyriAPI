package fr.hyriode.api.impl.common.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.HyriLeaderboardScope;
import fr.hyriode.api.leaderboard.HyriLeaderboardScore;
import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/06/2022 at 20:16
 */
public class HyriLeaderboard implements IHyriLeaderboard {

    private final String type;
    private final String name;

    public HyriLeaderboard(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private String getKey(HyriLeaderboardScope scope) {
        return "leaderboards:" + this.type + ":" + this.name + ":" + scope.getId();
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
    public List<UUID> getLeaders(HyriLeaderboardScope scope) {
        return this.getLeaders(scope, 0, -1);
    }

    @Override
    public List<UUID> getLeaders(HyriLeaderboardScope scope, long start, long stop) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<UUID> leaders = new ArrayList<>();

            for (String member : jedis.zrange(this.getKey(scope), start, stop)) {
                leaders.add(UUID.fromString(member));
            }
            return leaders;
        });
    }

    @Override
    public long getPosition(HyriLeaderboardScope scope, UUID id) {
        try {
            return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.zrevrank(this.getKey(scope), id.toString()));
        } catch (NullPointerException e) {
            return -1;
        }
    }

    @Override
    public List<HyriLeaderboardScore> getScores(HyriLeaderboardScope scope) {
        return this.getScores(scope, 0, -1);
    }

    @Override
    public List<HyriLeaderboardScore> getScores(HyriLeaderboardScope scope, long start, long stop) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<HyriLeaderboardScore> scores = new ArrayList<>();
            final List<Tuple> tuples = jedis.zrangeWithScores(this.getKey(scope), start, stop);

            for (Tuple tuple : tuples) {
                scores.add(new HyriLeaderboardScore(UUID.fromString(tuple.getElement()), tuple.getScore()));
            }

            Collections.reverse(scores);

            return scores;
        });
    }

    @Override
    public double getScore(HyriLeaderboardScope scope, UUID id) {
        try {
            return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.zscore(this.getKey(scope), id.toString()));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void setScore(HyriLeaderboardScope scope, UUID id, double score) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final String key = this.getKey(scope);

            jedis.zadd(key, score, id.toString());

            this.checkExpiration(key, scope, jedis);
        });
    }

    @Override
    public void incrementScore(HyriLeaderboardScope scope, UUID id, double score) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final String key = this.getKey(scope);

            jedis.zincrby(key, score, id.toString());

            this.checkExpiration(key, scope, jedis);
        });
    }

    @Override
    public void removeScore(HyriLeaderboardScope scope, UUID id) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final String key = this.getKey(scope);

            jedis.zrem(key, id.toString());

            this.checkExpiration(key, scope, jedis);
        });
    }

    @Override
    public void clear(HyriLeaderboardScope scope) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(this.getKey(scope)));
    }

    private void checkExpiration(String key, HyriLeaderboardScope scope, Jedis jedis) {
        final long nextReset = scope.nextReset();

        if (nextReset < 0) {
            return;
        }

        final long ttl = jedis.ttl(key);

        if (ttl == -1) {
            jedis.expire(key, nextReset * 1000L);
        }
    }

}
