package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.server.IServerManager;
import fr.hyriode.hyriapi.server.AbstractServer;
import redis.clients.jedis.Jedis;

public abstract class HyriAPI {

    /**
     * {@link HyriAPI} instance static field
     */
    private static HyriAPI instance;

    /**
     * Constructor of {@link HyriAPI}
     */
    public HyriAPI() {
        instance = this;
    }

    /**
     * Get the current server where HyriAPI is running (ex: lobby-dsf546)
     *
     * @return - Current server
     */
    public abstract AbstractServer getServer();

    /**
     * Get a resource of {@link Jedis}
     *
     * @return - {@link Jedis} resource
     */
    public abstract Jedis getJedisResource();

    /**
     * Get the server manager
     *
     * @return - {@link IServerManager}
     */
    public abstract IServerManager getServerManager();

    /**
     * Get the instance of {@link HyriAPI}
     *
     * @return - HyriAPI instance
     */
    public static HyriAPI get() {
        return instance;
    }

}
