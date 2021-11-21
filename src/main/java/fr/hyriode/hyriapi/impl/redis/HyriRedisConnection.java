package fr.hyriode.hyriapi.impl.redis;

import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import fr.hyriode.hyriapi.impl.configuration.nested.RedisConfiguration;
import fr.hyriode.hyriapi.redis.IHyriRedisConnection;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriRedisConnection implements IHyriRedisConnection {

    private boolean connected;

    private JedisPool jedisPool;

    private final HyriAPIPlugin plugin;

    public HyriRedisConnection(HyriAPIPlugin plugin) {
        this.plugin = plugin;

        this.start();
    }

    private void start() {
        this.connect();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                this.getResource();
            } catch (Exception e) {
                HyriAPIPlugin.log(Level.SEVERE, "An error occurred in Redis connection ! Trying to reconnect...");

                this.connect();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void connect() {
        final JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(-1);
        config.setJmxEnabled(false);

        final RedisConfiguration configuration = this.plugin.getConfiguration().getRedisConfiguration();

        this.jedisPool = new JedisPool(config, configuration.getIp(), configuration.getPort(), 2000, configuration.getPassword());

        try {
            this.getResource();

            this.connected = true;

            HyriAPIPlugin.log("Connection set between " + this.plugin.getName() + " and Redis");
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "An error occurred during connecting to Redis! ", e);
            HyriAPIPlugin.log(Level.SEVERE, "Try to fix it! Bukkit is now stopping...");

            Bukkit.shutdown();
        }
    }

    public void stop() {
        this.jedisPool.close();
        this.jedisPool.destroy();
    }

    @Override
    public Jedis getResource() {
        return this.jedisPool.getResource();
    }

    @Override
    public JedisPool getPool() {
        return this.jedisPool;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

}
