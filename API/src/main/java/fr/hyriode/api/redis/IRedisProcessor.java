package fr.hyriode.api.redis;

import redis.clients.jedis.Jedis;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/11/2021 at 14:06
 */
public interface IRedisProcessor {

    /**
     * Process a redis action with {@link Jedis}
     *
     * @param action The action to execute
     */
    void process(Consumer<Jedis> action);

    /**
     * Process a redis action with {@link Jedis} but with a returned value
     *
     * @param action The action to execute
     */
    void processAsync(Consumer<Jedis> action);

    /**
     * Process a redis action with {@link Jedis} but with a returned value
     *
     * @param action The action to execute
     * @param <R> The type of the returned value
     * @return The value retrieved from Redis
     */
    <R> R get(Function<Jedis, R> action);

}
