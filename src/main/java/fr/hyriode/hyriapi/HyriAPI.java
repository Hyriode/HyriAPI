package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.server.IHyriServer;
import fr.hyriode.hyriapi.server.IHyriServerManager;
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
     * Get a resource of {@link Jedis}
     *
     * @return - {@link Jedis} resource
     */
    public abstract Jedis getJedisResource();


    /**
     * Get the current server where HyriAPI is running (ex: lobby-dsf546)
     *
     * @return - Current server
     */
    public abstract IHyriServer getServer();

    /**
     * Get the server manager
     *
     * @return - {@link IHyriServerManager}
     */
    public abstract IHyriServerManager getServerManager();

    /**
     * Get the player manager
     *
     * @return - {@link IHyriPlayerManager}
     */
    public abstract IHyriPlayerManager getPlayerManager();

    /**
     * Get the money manager
     *
     * @return - {@link IHyriMoneyManager}
     */
    public abstract IHyriMoneyManager getMoneyManager();

    /**
     * Get the instance of {@link HyriAPI}
     *
     * @return - HyriAPI instance
     */
    public static HyriAPI get() {
        return instance;
    }

}
