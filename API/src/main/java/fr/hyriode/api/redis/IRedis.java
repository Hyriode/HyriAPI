package fr.hyriode.api.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/11/2021 at 14:03
 */
public interface IRedis {

    /**
     * Get a resource from Redis database
     *
     * @return A {@link Jedis} object
     */
    Jedis getResource();

    /**
     * Get Redis pool
     *
     * @return {@link JedisPool} instance
     */
    JedisPool getPool();

    /**
     * Check if the connection is set between Redis and HyriAPI
     *
     * @return <code>true</code> if yes
     */
    boolean isConnected();

    /**
     * Close the current instance of {@link IRedis}
     *
     * @return A new {@link IRedis} with the same data as the previous one
     */
    IRedis clone();

}
