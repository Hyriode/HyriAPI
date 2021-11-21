package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.cosmetic.IHyriCosmeticManager;
import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.party.IHyriPartyManager;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.pubsub.IHyriPubSub;
import fr.hyriode.hyriapi.rank.IHyriRankManager;
import fr.hyriode.hyriapi.redis.IHyriRedisConnection;
import fr.hyriode.hyriapi.redis.IHyriRedisProcessor;
import fr.hyriode.hyriapi.server.IHyriServer;
import fr.hyriode.hyriapi.server.IHyriServerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettingsManager;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class HyriAPI {

    public static final String NAME = "HyriAPI";

    /**
     * {@link HyriAPI} instance static field
     */
    private static HyriAPI instance;

    public HyriAPI(Logger logger) {
        instance = this;

        logger.log(Level.INFO, "Registered '" + this.getClass().getName() + "' as an implementation of " + NAME + ".");
    }

    /**
     * Get a resource of {@link Jedis}
     *
     * @return {@link Jedis} resource
     */
    public abstract Jedis getRedisResource();

    /**
     * Get the Redis connection
     *
     * @return {@link IHyriRedisConnection}
     */
    public abstract IHyriRedisConnection getRedisConnection();

    /**
     * Get the Redis processor
     *
     * @return {@link IHyriRedisProcessor}
     */
    public abstract IHyriRedisProcessor getRedisProcessor();

    /**
     * Get the Redis PubSub manager
     *
     * @return {@link IHyriPubSub}
     */
    public abstract IHyriPubSub getPubSub();

    /**
     * Get the current server where HyriAPI is running (ex: lobby-d7g2)
     *
     * @return Current server
     */
    public abstract IHyriServer getServer();

    /**
     * Get the server manager
     *
     * @return {@link IHyriServerManager}
     */
    public abstract IHyriServerManager getServerManager();

    /**
     * Get the player manager
     *
     * @return {@link IHyriPlayerManager}
     */
    public abstract IHyriPlayerManager getPlayerManager();

    /**
     * Get the player settings manager
     *
     * @return {@link IHyriPlayerSettingsManager}
     */
    public abstract IHyriPlayerSettingsManager getPlayerSettingsManager();

    /**
     * Get the money manager
     *
     * @return {@link IHyriMoneyManager}
     */
    public abstract IHyriMoneyManager getMoneyManager();

    /**
     * Get the party manager
     *
     * @return {@link IHyriPartyManager}
     */
    public abstract IHyriPartyManager getPartyManager();

    /**
     * Get the rank manager
     *
     * @return {@link IHyriRankManager}
     */
    public abstract IHyriRankManager getRankManager();

    /**
     * Get the cosmetics manager
     * @return {@link IHyriCosmeticManager}
     */
    public abstract IHyriCosmeticManager getCosmeticManager();

    /**
     * Get the instance of {@link HyriAPI}
     *
     * @return HyriAPI instance
     */
    public static HyriAPI get() {
        return instance;
    }

}
