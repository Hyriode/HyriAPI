package fr.hyriode.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.config.IHyriConfigManager;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.game.IHyriGameManager;
import fr.hyriode.api.guild.IHyriGuildManager;
import fr.hyriode.api.host.IHostConfigManager;
import fr.hyriode.api.host.IHostManager;
import fr.hyriode.api.http.IHttpRequester;
import fr.hyriode.api.hyggdrasil.IHyggdrasilManager;
import fr.hyriode.api.language.IHyriLanguageManager;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;
import fr.hyriode.api.limbo.IHyriLimbo;
import fr.hyriode.api.limbo.IHyriLimboManager;
import fr.hyriode.api.lootbox.IHyriLootboxManager;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.mongodb.IMongoDB;
import fr.hyriode.api.network.IHyriNetworkManager;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.proxy.IHyriProxyManager;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.api.redis.IRedis;
import fr.hyriode.api.redis.IRedisProcessor;
import fr.hyriode.api.scheduler.IHyriScheduler;
import fr.hyriode.api.serialization.ClassSerializer;
import fr.hyriode.api.serialization.DataSerializer;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.api.server.ILobbyAPI;
import fr.hyriode.api.server.join.IHyriJoinManager;
import fr.hyriode.api.util.HyriAPIException;
import fr.hyriode.api.world.IHyriWorldManager;
import fr.hyriode.api.world.generation.IWorldGenerationAPI;
import fr.hyriode.hyreos.api.HyreosAPI;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class HyriAPI {

    /** {@link Gson} instance with adapters */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(HyriPacket.class, new ClassSerializer<HyriPacket>())
            .create();

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
     * Register the implementation of {@link HyriAPI}
     *
     * @param implementation The {@link HyriAPI} instance
     * @param <I> The type of the implementation to register
     */
    public static <I extends HyriAPI> void register(I implementation) {
        if (instance != null) {
            throw new HyriAPIException("HyriAPI implementation is already registered!");
        }

        instance = implementation;
    }

    /**
     * Log a message as HyriAPI.
     *
     * @param level The level of the message to log
     * @param message The message to log
     */
    public abstract void log(Level level, String message);

    /**
     * Log a message as HyriAPI.
     *
     * @param message The message to log
     */
    public void log(String message) {
        this.log(Level.INFO, message);
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
     * Get the current limbo where HyriAPI is running
     *
     * @return The current {@link IHyriLimbo} instance
     */
    public abstract IHyriLimbo getLimbo();

    /**
     * Check whether HyriAPI is running on a limbo
     *
     * @return <code>true</code> if yes, <code>false</code> otherwise
     */
    public boolean isLimbo() {
        return this.getLimbo() != null;
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
     * @return {@link IRedis}
     */
    public abstract IRedis getRedisConnection();

    /**
     * Get the Redis processor
     *
     * @return {@link IRedisProcessor}
     */
    public abstract IRedisProcessor getRedisProcessor();

    /**
     * Get the MongoDB database instance manager
     *
     * @return The {@link IMongoDB} instance
     */
    public abstract IMongoDB getMongoDB();

    /**
     * Get the HTTP requester instance
     *
     * @return The {@link IHttpRequester} instance
     */
    public abstract IHttpRequester getHttpRequester();

    /**
     * Get the data serializer instance
     *
     * @return The {@link DataSerializer} instance
     */
    public abstract DataSerializer getDataSerializer();

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
     * Get the Hyreos API instance
     *
     * @return The {@link HyreosAPI} instance
     */
    public abstract HyreosAPI getHyreosAPI();

    /**
     * Get the manager of configurations stored in database
     *
     * @return The {@link IHyriConfigManager} instance
     */
    public abstract IHyriConfigManager getConfigManager();

    /**
     * Get the class that can be used to get or edit information of the network
     *
     * @return The {@link IHyriNetworkManager} instance
     */
    public abstract IHyriNetworkManager getNetworkManager();

    /**
     * Get the server manager
     *
     * @return The {@link IHyriServerManager} instance
     */
    public abstract IHyriServerManager getServerManager();

    /**
     * Get the join manager instance
     *
     * @return The {@link IHyriJoinManager} instance
     */
    public abstract IHyriJoinManager getJoinManager();

    /**
     * Get the API related to lobby's system.
     *
     * @return The {@link ILobbyAPI} instance
     */
    public abstract ILobbyAPI getLobbyAPI();

    /**
     * Get the manager related to world storing
     *
     * @return The {@link IHyriWorldManager} instance
     */
    public abstract IHyriWorldManager getWorldManager();

    /**
     * Get the API related to the world generation system.
     *
     * @return The {@link IWorldGenerationAPI} instance
     */
    public abstract IWorldGenerationAPI getWorldGenerationAPI();

    /**
     * Get the proxy manager instance
     *
     * @return The {@link IHyriProxyManager} instance
     */
    public abstract IHyriProxyManager getProxyManager();

    /**
     * Get the limbo manager instance
     *
     * @return The {@link IHyriLimboManager} instance
     */
    public abstract IHyriLimboManager getLimboManager();

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
     * @return The {@link IHyriPlayerManager} instance
     */
    public abstract IHyriPlayerManager getPlayerManager();

    /**
     * Get the guild manager
     *
     * @return The {@link IHyriGuildManager} instance
     */
    public abstract IHyriGuildManager getGuildManager();

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
     * Get the host manager instance
     *
     * @return The {@link IHostManager} instance
     */
    public abstract IHostManager getHostManager();

    /**
     * Get the {@link IHostConfigManager} instance
     *
     * @return The {@link IHostConfigManager} instance
     */
    public abstract IHostConfigManager getHostConfigManager();

    /**
     * Get the {@link IHyriLootboxManager} instance
     *
     * @return The {@link IHyriLootboxManager} instance
     */
    public abstract IHyriLootboxManager getLootboxManager();

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

    private static final class NotRegisteredException extends HyriAPIException {

        public NotRegisteredException() {
            super(NAME + " has not been registered yet!\n" +
                    "\n        These might be the following reasons:\n" +
                    "          1) The " + NAME + " plugin is not installed or it failed while enabling\n" +
                    "          2) Your plugin is loading before " + NAME + "\n" +
                    "          3) No implementation of " + NAME + " exists\n");
        }

    }

}
