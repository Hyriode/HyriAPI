package fr.hyriode.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.game.IHyriGameManager;
import fr.hyriode.api.host.IHostConfigManager;
import fr.hyriode.api.hyggdrasil.IHyggdrasilManager;
import fr.hyriode.api.language.IHyriLanguageManager;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.mongodb.IHyriMongoDB;
import fr.hyriode.api.network.IHyriNetworkManager;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.proxy.IHyriProxyManager;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.api.redis.IHyriRedisConnection;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.api.scheduler.IHyriScheduler;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.api.settings.IHyriPlayerSettingsManager;
import fr.hyriode.hylios.api.HyliosAPI;
import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hystia.api.IHystiaAPI;
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
     * @return The {@link IHyriAPIConfig} instance
     */
    public abstract IHyriAPIConfig getConfig();

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
     * Get the MongoDB database instance manager
     *
     * @return The {@link IHyriMongoDB} instance
     */
    public abstract IHyriMongoDB getMongoDB();

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
     * Get the scheduler instance used to run tasks
     *
     * @return The {@link IHyriScheduler} instance
     */
    public abstract IHyriScheduler getScheduler();

    /**
     * Get the Hyggdrasil manager instance
     *
     * @return The {@link IHyggdrasilManager} instance
     */
    public abstract IHyggdrasilManager getHyggdrasilManager();

    /**
     * Get the instance of Hystia API
     *
     * @return The {@link IHystiaAPI} instance
     */
    public abstract IHystiaAPI getHystiaAPI();

    /**
     * Get the Hylios API instance
     *
     * @return The {@link HyliosAPI} instance
     */
    public abstract HyliosAPI getHyliosAPI();

    /**
     * Get the Hyreos API instance
     *
     * @return The {@link HyreosAPI} instance
     */
    public abstract HyreosAPI getHyreosAPI();

    /**
     * Get the class that can be used to get or edit information of the network
     *
     * @return The {@link IHyriNetworkManager} instance
     */
    public abstract IHyriNetworkManager getNetworkManager();

    /**
     * Get the server manager
     *
     * @return {@link IHyriServerManager}
     */
    public abstract IHyriServerManager getServerManager();

    /**
     * Get the proxy manager instance
     *
     * @return The {@link IHyriProxyManager} instance
     */
    public abstract IHyriProxyManager getProxyManager();

    /**
     * Get the queue manager
     *
     * @return The {@link IHyriQueueManager} instance
     */
    public abstract IHyriQueueManager getQueueManager();

    /**
     * Get the game manager
     *
     * @return The {@link IHyriGameManager} instance
     */
    public abstract IHyriGameManager getGameManager();

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
     * Get the language manager instance; it contains all languages-API methods
     *
     * @return The {@linkplain IHyriLanguageManager language manager} instance
     */
    public abstract IHyriLanguageManager getLanguageManager();

    /**
     * Get the booster manager
     *
     * @return The {@link IHyriBoosterManager} instance
     */
    public abstract IHyriBoosterManager getBoosterManager();

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
     * Get the friend manager instance
     *
     * @return The {@link IHyriFriendManager} instance
     */
    public abstract IHyriFriendManager getFriendManager();

    /**
     * Get the chat manager
     *
     * @return {@link IHyriChatChannelManager}
     */
    public abstract IHyriChatChannelManager getChatChannelManager();

    /**
     * Get the leaderboard provider instance
     *
     * @return The {@linkplain IHyriLeaderboardProvider leaderboard provider} instance
     */
    public abstract IHyriLeaderboardProvider getLeaderboardProvider();

    /**
     * Get the {@link IHostConfigManager} instance
     *
     * @return The {@link IHostConfigManager} instance
     */
    public abstract IHostConfigManager getHostConfigManager();

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
