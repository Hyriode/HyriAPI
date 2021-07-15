package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.tools.npc.NPCManager;
import redis.clients.jedis.Jedis;

public abstract class HyriAPI {

    private static HyriAPI instance;

    public HyriAPI() {
        instance = this;
    }

    public abstract Jedis getJedisResource();

    public abstract NPCManager getNPCManager();

    public static HyriAPI get() {
        return instance;
    }

}
