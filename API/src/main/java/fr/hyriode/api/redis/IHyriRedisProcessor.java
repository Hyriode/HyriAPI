package fr.hyriode.api.redis;

import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/11/2021 at 14:06
 */
public interface IHyriRedisProcessor {

    /**
     * Add a Jedis to a queue to handle requests traffic
     *
     * @param action Action to execute
     */
    void process(Consumer<Jedis> action);

    /**
     * Add a Jedis to a queue to handle requests traffic
     *
     * @param action Action to execute
     * @param callback Callback to call after execution
     */
    void process(Consumer<Jedis> action, Runnable callback);

}
