package fr.hyriode.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import fr.hyriode.api.cosmetic.IHyriCosmeticManager;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.player.pubsub.IHyriPubSub;
import fr.hyriode.api.rank.IHyriRankManager;
import fr.hyriode.api.redis.IHyriRedisConnection;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.api.settings.IHyriPlayerSettingsManager;
import redis.clients.jedis.Jedis;

public abstract class HyriAPI {

    /** {@link Gson} instance with adapters */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(HyriPacket.class, new HyriPacket.Serializer())
            .create();
    /** A default {@link Gson} instance */
    public static final Gson NORMAL_GSON = new Gson();

    /** ASCII header lines */
    public static final String[] HEADER_LINES = new String[]{
            "  _  _          _   _   ___ ___ ",
            " | || |_  _ _ _(_) /_\\ | _ \\_ _|",
            " | __ | || | '_| |/ _ \\|  _/| | ",
            " |_||_|\\_, |_| |_/_/ \\_\\_| |___|",
            "       |__/                     "
    };

    /** The application name */
    public static final String NAME = "HyriAPI";

    /*** {@link HyriAPI} static instance field */
    private static HyriAPI instance;

    /**
     * Empty constructor of {@link HyriAPI}
     */
    public HyriAPI() {
        instance = this;
    }

    /**
     * Get the current server where HyriAPI is running (ex: lobby-d7g2)
     *
     * @return The current {@link IHyriServer} object
     */
    public abstract IHyriServer getServer();

    /**
     * Check if HyriAPI is running on a server
     *
     * @return <code>true</code> if HyriAPI is running on a server
     */
    public boolean isServer() {
        return this.getServer() != null;
    }

    /**
     * Get the current proxy where HyriAPI is running
     *
     * @return The current {@link IHyriProxy} object
     */
    public abstract IHyriProxy getProxy();

    /**
     * Check if HyriAPI is running on a proxy
     *
     * @return <code>true</code> if HyriAPI is running on a proxy
     */
    public boolean isProxy() {
        return this.getProxy() != null;
    }

    /**
     * Get the configuration of HyriAPI
     *
     * @return The {@link IHyriAPIConfiguration} instance
     */
    public abstract IHyriAPIConfiguration getConfiguration();

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
     * Get the default event bus
     *
     * @return The default {@link IHyriEventBus} instance
     */
    public abstract IHyriEventBus getEventBus();

    /**
     * Get the Redis PubSub manager
     *
     * @return {@link IHyriPubSub}
     */
    public abstract IHyriPubSub getPubSub();

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
        if (instance == null) {
            throw new NotRegisteredException();
        }
        return instance;
    }

    private static final class NotRegisteredException extends IllegalStateException {

        public NotRegisteredException() {
            super(NAME + " has not been registered yet!\n" +
                    "\n        These might be the following reasons:\n" +
                    "          1) The " + NAME + " plugin is not installed or it failed while enabling\n" +
                    "          2) Your plugin is loading before " + NAME + "\n" +
                    "          3) No implementation of " + NAME + " exists\n");
        }

    }

}
