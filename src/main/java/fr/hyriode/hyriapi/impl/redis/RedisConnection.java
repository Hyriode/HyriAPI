package fr.hyriode.hyriapi.impl.redis;

import fr.hyriode.hyriapi.impl.configuration.nested.RedisConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

    private final JedisPool jedisPool;

    public RedisConnection(String host, int port, String password) {
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        config.setMaxTotal(-1);

        this.jedisPool = new JedisPool(config, host, port, 5000, password);
    }

    public RedisConnection(RedisConfiguration redisConfiguration) {
        this(redisConfiguration.getRedisIp(), redisConfiguration.getRedisPort(), redisConfiguration.getRedisPassword());
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

}
