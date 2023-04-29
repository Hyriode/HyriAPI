package fr.hyriode.api.impl.common;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.event.HyriEventBus;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.host.IHostConfigManager;
import fr.hyriode.api.host.IHostManager;
import fr.hyriode.api.impl.common.booster.HyriBoosterManager;
import fr.hyriode.api.impl.common.chat.HyriChatChannelManager;
import fr.hyriode.api.impl.common.config.HyriConfigManager;
import fr.hyriode.api.impl.common.game.HyriGameManager;
import fr.hyriode.api.impl.common.guild.HyriGuildManager;
import fr.hyriode.api.impl.common.host.HostConfigManager;
import fr.hyriode.api.impl.common.host.HostManager;
import fr.hyriode.api.impl.common.http.HttpRequester;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.common.language.HyriLanguageManager;
import fr.hyriode.api.impl.common.leaderboard.HyriLeaderboardProvider;
import fr.hyriode.api.impl.common.limbo.HyriLimboManager;
import fr.hyriode.api.impl.common.lootbox.HyriLootboxManager;
import fr.hyriode.api.impl.common.money.HyriMoneyManager;
import fr.hyriode.api.impl.common.mongodb.MongoDB;
import fr.hyriode.api.impl.common.network.HyriNetworkManager;
import fr.hyriode.api.impl.common.party.HyriPartyManager;
import fr.hyriode.api.impl.common.player.HyriPlayerManager;
import fr.hyriode.api.impl.common.proxy.HyriProxyManager;
import fr.hyriode.api.impl.common.pubsub.HyriPubSub;
import fr.hyriode.api.impl.common.queue.HyriQueueManager;
import fr.hyriode.api.impl.common.redis.Redis;
import fr.hyriode.api.impl.common.redis.RedisProcessor;
import fr.hyriode.api.impl.common.scheduler.HyriScheduler;
import fr.hyriode.api.impl.common.serialization.DataSerializerImpl;
import fr.hyriode.api.impl.common.server.HyriServerManager;
import fr.hyriode.api.impl.common.server.LobbyAPI;
import fr.hyriode.api.impl.common.world.HyriWorldManager;
import fr.hyriode.api.impl.common.world.generation.WorldGenerationAPI;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;
import fr.hyriode.api.limbo.IHyriLimbo;
import fr.hyriode.api.lootbox.IHyriLootboxManager;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.mongodb.IMongoDB;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.serialization.DataSerializer;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.server.join.IHyriJoinManager;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggEnv;
import fr.hyriode.hyreos.api.HyreosAPI;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 15:47
 */
public abstract class CHyriAPIImpl extends HyriAPI {

    protected IHyriAPIConfig configuration;

    protected Redis redisConnection;
    protected RedisProcessor redisProcessor;
    protected MongoDB mongoDB;

    protected HttpRequester httpRequester;

    protected DataSerializer dataSerializer;

    protected IHyriEventBus eventBus;
    protected HyriPubSub pubSub;
    protected HyriScheduler scheduler;

    protected HyggdrasilManager hyggdrasilManager;
    protected HyreosAPI hyreosAPI;

    protected HyriConfigManager configManager;
    protected HyriNetworkManager networkManager;
    protected HyriProxyManager proxyManager;
    protected HyriLimboManager limboManager;
    protected HyriServerManager serverManager;
    protected LobbyAPI lobbyAPI;
    protected HyriWorldManager worldManager;
    protected WorldGenerationAPI worldGenerationAPI;
    protected HyriQueueManager queueManager;
    protected HyriGameManager gameManager;

    protected HyriPlayerManager playerManager;
    protected HyriGuildManager guildManager;
    protected HyriLanguageManager languageManager;

    protected IHyriBoosterManager boosterManager;
    protected IHyriMoneyManager moneyManager;

    protected IHyriPartyManager partyManager;

    protected IHyriChatChannelManager chatChannelManager;

    protected IHyriLeaderboardProvider leaderboardProvider;

    protected IHostManager hostManager;
    protected IHostConfigManager hostConfigManager;

    protected IHyriLootboxManager lootboxManager;

    public CHyriAPIImpl(IHyriAPIConfig config) {
        this.configuration = config;
    }

    protected void preInit() {
        HyriAPI.register(this);

        for (String line : HEADER_LINES) {
            this.log(line);
        }

        this.log("Registered " + this.getClass().getName() + " as an implementation of " + NAME + ".");
    }

    protected void init(HyggEnv environment, Logger logger) {
        this.scheduler = new HyriScheduler();

        // Databases connections
        this.redisConnection = new Redis(this);
        this.redisProcessor = new RedisProcessor();
        this.mongoDB = new MongoDB(this.configuration.getMongoDBConfig());
        this.mongoDB.startConnection();

        /// Http
        this.httpRequester = new HttpRequester();

        // Data storage
        this.dataSerializer = new DataSerializerImpl();

        // Internal systems
        this.eventBus = new HyriEventBus("default");
        this.pubSub = new HyriPubSub();
        this.hyreosAPI = new HyreosAPI(this.pubSub.getRedisConnection().getPool());
        this.hyreosAPI.start();

        // Hyggdrasil and servers/proxies management
        this.hyggdrasilManager = new HyggdrasilManager(environment);
        this.hyggdrasilManager.start(logger);
        this.proxyManager = new HyriProxyManager();
        this.limboManager = new HyriLimboManager();
        this.serverManager = new HyriServerManager();
        this.lobbyAPI = new LobbyAPI();

        // Managers initialization
        this.configManager = new HyriConfigManager(this.mongoDB);
        this.networkManager = new HyriNetworkManager();
        if (!this.isServer()) {
            this.worldManager = new HyriWorldManager(this.mongoDB);
        }
        this.worldGenerationAPI = new WorldGenerationAPI();
        this.queueManager = new HyriQueueManager();
        this.gameManager = new HyriGameManager();
        if (!this.isServer() && !this.isProxy()) {
            this.playerManager = new HyriPlayerManager();
        }
        this.guildManager = new HyriGuildManager();
        this.languageManager = new HyriLanguageManager();
        this.boosterManager = new HyriBoosterManager();
        this.moneyManager = new HyriMoneyManager();
        this.partyManager = new HyriPartyManager();
        this.chatChannelManager = new HyriChatChannelManager();
        this.leaderboardProvider = new HyriLeaderboardProvider();
        this.hostManager = new HostManager();
        this.hostConfigManager = new HostConfigManager();
        this.lootboxManager = new HyriLootboxManager();
    }

    protected void postInit() {
        this.hyggdrasilManager.registerListeners();
        this.languageManager.registerAdapter(IHyriPlayer.class, (message, account) -> message.getValue(account.getSettings().getLanguage()));
        this.languageManager.registerAdapter(UUID.class, (message, uuid) -> {
            final HyriLanguage cachedLanguage = this.languageManager.getCache(uuid);

            if (cachedLanguage != null) {
                return message.getValue(cachedLanguage);
            }

            final IHyriPlayer account = IHyriPlayer.get(uuid);

            return account != null ? message.getValue(account) : message.getValue(HyriLanguage.FR);
        });
        this.hyggdrasilManager.sendData();
    }

    public void stop() {
        this.httpRequester.stop();

        if (this.redisConnection.isConnected()) {
            this.pubSub.stop();
            this.redisConnection.stop();
        }

        this.hyreosAPI.stop();
        this.mongoDB.stopConnection();
        this.scheduler.stop();
        this.hyggdrasilManager.stop();
    }

    @Override
    public IHyriAPIConfig getConfig() {
        return this.configuration;
    }

    @Override
    public IHyriServer getServer() {
        return null;
    }

    @Override
    public IHyriProxy getProxy() {
        return null;
    }

    @Override
    public IHyriLimbo getLimbo() {
        return null;
    }

    @Override
    public Jedis getRedisResource() {
        return this.redisConnection.getResource();
    }

    @Override
    public Redis getRedisConnection() {
        return this.redisConnection;
    }

    @Override
    public RedisProcessor getRedisProcessor() {
        return this.redisProcessor;
    }

    @Override
    public IMongoDB getMongoDB() {
        return this.mongoDB;
    }

    @Override
    public HttpRequester getHttpRequester() {
        return this.httpRequester;
    }

    @Override
    public DataSerializer getDataSerializer() {
        return this.dataSerializer;
    }

    @Override
    public IHyriEventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public HyriPubSub getPubSub() {
        return this.pubSub;
    }

    @Override
    public HyriScheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public HyggdrasilManager getHyggdrasilManager() {
        return this.hyggdrasilManager;
    }

    @Override
    public HyreosAPI getHyreosAPI() {
        return this.hyreosAPI;
    }

    @Override
    public HyriConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public HyriNetworkManager getNetworkManager() {
        return this.networkManager;
    }

    @Override
    public HyriServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public LobbyAPI getLobbyAPI() {
        return this.lobbyAPI;
    }

    @Override
    public HyriWorldManager getWorldManager() {
        return this.worldManager;
    }

    @Override
    public WorldGenerationAPI getWorldGenerationAPI() {
        return this.worldGenerationAPI;
    }

    @Override
    public HyriLimboManager getLimboManager() {
        return this.limboManager;
    }

    @Override
    public HyriProxyManager getProxyManager() {
        return this.proxyManager;
    }

    @Override
    public HyriQueueManager getQueueManager() {
        return this.queueManager;
    }

    @Override
    public HyriGameManager getGameManager() {
        return this.gameManager;
    }

    @Override
    public HyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public HyriGuildManager getGuildManager() {
        return this.guildManager;
    }

    @Override
    public HyriLanguageManager getLanguageManager() {
        return this.languageManager;
    }

    @Override
    public IHyriBoosterManager getBoosterManager() {
        return this.boosterManager;
    }

    @Override
    public IHyriMoneyManager getMoneyManager() {
        return this.moneyManager;
    }

    @Override
    public IHyriPartyManager getPartyManager() {
        return this.partyManager;
    }

    @Override
    public IHyriChatChannelManager getChatChannelManager() {
        return this.chatChannelManager;
    }

    @Override
    public IHyriLeaderboardProvider getLeaderboardProvider() {
        return this.leaderboardProvider;
    }

    @Override
    public IHostManager getHostManager() {
        return this.hostManager;
    }

    @Override
    public IHostConfigManager getHostConfigManager() {
        return this.hostConfigManager;
    }

    @Override
    public IHyriLootboxManager getLootboxManager() {
        return this.lootboxManager;
    }

    @Override
    public IHyriJoinManager getJoinManager() {
        return null;
    }

}
