package fr.hyriode.api.impl.common.redis;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.RedisConfig;
import fr.hyriode.api.impl.common.CHyriAPIImpl;
import fr.hyriode.api.redis.IRedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class Redis implements IRedis {

    private boolean running;

    private boolean connected;

    private JedisPool jedisPool;

    private final String hostname;
    private final int port;
    private final String password;

    private final CHyriAPIImpl api;

    public Redis(CHyriAPIImpl api) {
        this.api = api;

        final RedisConfig redisConfig = this.api.getConfig().getRedisConfig();

        this.hostname = redisConfig.getHostname();
        this.port = redisConfig.getPort();
        this.password = redisConfig.getPassword();

        this.start();
    }

    private void start() {
        this.running = true;

        this.connect();

        HyriAPI.get().getScheduler().schedule(() -> {
            try {
                this.getResource().close();
            } catch (Exception e) {
                HyriAPI.get().log(Level.SEVERE, "An error occurred in Redis connection ! Trying to reconnect...");

                this.connect();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void connect() {
        final JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(-1);
        config.setJmxEnabled(false);

        if (this.password == null) {
            this.jedisPool = new JedisPool(config, this.hostname, this.port, 0);
        } else {
            this.jedisPool = new JedisPool(config, this.hostname, this.port, 0, this.password);
        }

        try {
            this.getResource().close();

            this.connected = true;

            HyriAPI.get().log("Connection set between " + HyriAPI.NAME + " and Redis");
        } catch (Exception e) {
            HyriAPI.get().log(Level.SEVERE, "An error occurred while connecting to Redis! ");
            HyriAPI.get().log(Level.SEVERE, "Try to fix it! Application is now stopping...");
            e.printStackTrace();

            System.exit(-1);
        }
    }

    public void stop() {
        if (this.running) {
            HyriAPI.get().log("Stopping Redis connection...");

            this.running = false;

            this.jedisPool.close();
            this.jedisPool.destroy();
        }
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

    @Override
    public Redis clone() {
        return new Redis(this.api);
    }

}
