package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.server.Server;
import redis.clients.jedis.Jedis;

public abstract class HyriAPI {

    private static HyriAPI instance;

    public HyriAPI() {
        instance = this;
    }

    public abstract Server getServer();

    public abstract Jedis getJedisResource();

    public static HyriAPI get() {
        return instance;
    }

}
