package fr.hyriode.hyriapi.impl;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.configuration.Configuration;
import fr.hyriode.hyriapi.impl.configuration.ConfigurationManager;
import fr.hyriode.hyriapi.impl.redis.RedisConnection;
import fr.hyriode.hyriapi.tools.npc.NPCManager;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

public class HyriAPIImplementation extends HyriAPI {

    /** NPC */
    private final NPCManager npcManager;

    /** Redis */
    private final RedisConnection redisConnection;

    /** Configuration */
    private final ConfigurationManager configurationManager;

    public HyriAPIImplementation(JavaPlugin plugin) {
        super(plugin);

        this.configurationManager = new ConfigurationManager(true);

        this.redisConnection = new RedisConnection(this.getConfiguration().getRedisConfiguration());

        this.npcManager = new NPCManager();
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public Configuration getConfiguration() {
        return this.configurationManager.getConfiguration();
    }

    @Override
    public Jedis getJedisResource() {
        return this.redisConnection.getJedis();
    }

    @Override
    public NPCManager getNPCManager() {
        return this.npcManager;
    }

}
