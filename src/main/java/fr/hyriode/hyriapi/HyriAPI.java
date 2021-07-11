package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.tools.npc.NPCManager;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

public abstract class HyriAPI {

    private static HyriAPI instance;

    private final JavaPlugin plugin;

    public HyriAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public abstract Jedis getJedisResource();

    public abstract NPCManager getNPCManager();

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public static HyriAPI get() {
        return instance;
    }

}
