package fr.hyriode.api.impl.common.redis;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IRedisProcessor;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/11/2021 at 17:40
 */
public class RedisProcessor implements IRedisProcessor {

    @Override
    public void process(Consumer<Jedis> action) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            if (jedis != null) {
                action.accept(jedis);
            }
        }
    }

    @Override
    public void processAsync(Consumer<Jedis> action) {
        HyriAPI.get().getScheduler().runAsync(() -> this.process(action));
    }

    @Override
    public <R> R get(Function<Jedis, R> action) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            if (jedis != null) {
                return action.apply(jedis);
            }
        }
        return null;
    }

}
