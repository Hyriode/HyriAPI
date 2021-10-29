package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.cosmetics.IHyriCosmeticsManager;
import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.party.IHyriPartyManager;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.rank.IHyriRankManager;
import fr.hyriode.hyriapi.server.IHyriServer;
import fr.hyriode.hyriapi.server.IHyriServerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettingsManager;
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
     * Get the player settings manager
     *
     * @return - {@link IHyriPlayerSettingsManager}
     */
    public abstract IHyriPlayerSettingsManager getPlayerSettingsManager();

    /**
     * Get the money manager
     *
     * @return - {@link IHyriMoneyManager}
     */
    public abstract IHyriMoneyManager getMoneyManager();

    /**
     * Get the party manager
     *
     * @return - {@link IHyriPartyManager}
     */
    public abstract IHyriPartyManager getPartyManager();

    /**
     * Get the rank manager
     *
     * @return - {@link IHyriRankManager}
     */
    public abstract IHyriRankManager getRankManager();

    /**
     * Get the cosmetics manager
     * @return {@link IHyriCosmeticsManager}
     */

    public abstract IHyriCosmeticsManager getCosmeticsManager();

    /**
     * Get the instance of {@link HyriAPI}
     *
     * @return - HyriAPI instance
     */
    public static HyriAPI get() {
        return instance;
    }

}
