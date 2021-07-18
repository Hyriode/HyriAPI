package fr.hyriode.hyriapi.implementation.redis;

import fr.hyriode.hyriapi.implementation.HyriPlugin;
import fr.hyriode.hyriapi.implementation.configuration.nested.RedisConfiguration;
import fr.hyriode.hyriapi.implementation.thread.ThreadPool;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class RedisConnection {

    private JedisPool jedisPool;

    private final HyriPlugin plugin;

    public RedisConnection(HyriPlugin plugin) {
        this.plugin = plugin;

        start();
    }

    private void start() {
        this.connect();

        ThreadPool.EXECUTOR.scheduleAtFixedRate(() -> {
            try {
                this.getResource();
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "An error occurred in Redis connection ! Trying to reconnect...");

                this.connect();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void connect() {
        final JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(-1);
        config.setJmxEnabled(false);

        final RedisConfiguration configuration = this.plugin.getConfiguration().getRedisConfiguration();

        this.jedisPool = new JedisPool(config, configuration.getRedisIp(), configuration.getRedisPort(), 0, configuration.getRedisPassword());

        try {
            this.getResource();

            this.plugin.log("Connected to Redis !");
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "An error occurred during connecting to Redis ! ", e);
            this.plugin.log(Level.SEVERE, "Try to fix it ! Bukkit is now stopping...");

            Bukkit.shutdown();
        }
    }

    public void stop() {
        this.jedisPool.close();
        this.jedisPool.destroy();
    }

    public Jedis getResource() {
        return this.jedisPool.getResource();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

}
