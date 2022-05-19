package fr.hyriode.api.impl.common.redis;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.configuration.HyriRedisConfig;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.redis.IHyriRedisConnection;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggRedisCredentials;
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

    private boolean running;

    private boolean connected;

    private JedisPool jedisPool;

    private final String hostname;
    private final int port;
    private final String password;

    private final HyriCommonImplementation api;

    public HyriRedisConnection(HyriCommonImplementation api) {
        this.api = api;

        final IHyriAPIConfiguration config = this.api.getConfiguration();

        if (this.api.getConfiguration().withHyggdrasil()) {
            final HyggRedisCredentials credentials = this.api.getHyggdrasilManager().getEnvironment().getRedisCredentials();

            this.hostname = credentials.getHostname();
            this.port = credentials.getPort();
            this.password = credentials.getPassword();
        } else {
            final HyriRedisConfig redisConfig = config.getRedisConfig();

            this.hostname = redisConfig.getHostname();
            this.port = redisConfig.getPort();
            this.password = redisConfig.getPassword();
        }

        this.start();
    }

    private void start() {
        this.running = true;

        this.connect();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                this.getResource().close();
            } catch (Exception e) {
                HyriCommonImplementation.log(Level.SEVERE, "An error occurred in Redis connection ! Trying to reconnect...");

                this.connect();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void connect() {
        final JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(-1);
        config.setJmxEnabled(false);

        this.jedisPool = new JedisPool(config, this.hostname, this.port, 2000, this.password);

        try {
            this.getResource().close();

            this.connected = true;

            HyriCommonImplementation.log("Connection set between " + HyriAPI.NAME + " and Redis");
        } catch (Exception e) {
            HyriCommonImplementation.log(Level.SEVERE, "An error occurred while connecting to Redis! ");
            HyriCommonImplementation.log(Level.SEVERE, "Try to fix it! Bukkit is now stopping...");
            e.printStackTrace();

            System.exit(-1);
        }
    }

    public void stop() {
        if (this.running) {
            HyriCommonImplementation.log("Stopping Redis connection...");

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
    public HyriRedisConnection clone() {
        return new HyriRedisConnection(this.api);
    }

}
