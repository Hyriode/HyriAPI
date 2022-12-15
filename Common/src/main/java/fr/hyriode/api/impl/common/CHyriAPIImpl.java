package fr.hyriode.api.impl.common;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.event.HyriEventBus;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.host.IHostConfigManager;
import fr.hyriode.api.host.IHostManager;
import fr.hyriode.api.impl.common.booster.HyriBoosterManager;
import fr.hyriode.api.impl.common.chat.HyriChatChannelManager;
import fr.hyriode.api.impl.common.friend.HyriFriendManager;
import fr.hyriode.api.impl.common.game.HyriGameManager;
import fr.hyriode.api.impl.common.host.HostConfigManager;
import fr.hyriode.api.impl.common.host.HostManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.common.language.HyriLanguageManager;
import fr.hyriode.api.impl.common.leaderboard.HyriLeaderboardProvider;
import fr.hyriode.api.impl.common.lootbox.HyriLootboxManager;
import fr.hyriode.api.impl.common.money.HyriMoneyManager;
import fr.hyriode.api.impl.common.mongodb.HyriMongoDB;
import fr.hyriode.api.impl.common.network.HyriNetworkManager;
import fr.hyriode.api.impl.common.party.HyriPartyManager;
import fr.hyriode.api.impl.common.player.CHyriPlayerManager;
import fr.hyriode.api.impl.common.proxy.HyriProxyManager;
import fr.hyriode.api.impl.common.pubsub.HyriPubSub;
import fr.hyriode.api.impl.common.queue.HyriQueueManager;
import fr.hyriode.api.impl.common.redis.HyriRedisConnection;
import fr.hyriode.api.impl.common.redis.HyriRedisProcessor;
import fr.hyriode.api.impl.common.scheduler.HyriScheduler;
import fr.hyriode.api.impl.common.server.HyriServerManager;
import fr.hyriode.api.impl.common.server.LobbyAPI;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettingsManager;
import fr.hyriode.api.impl.common.world.generation.WorldGenerationAPI;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;
import fr.hyriode.api.lootbox.IHyriLootboxManager;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.mongodb.IMongoDB;
import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.settings.IHyriSettingsManager;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggEnv;
import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hystia.api.IHystiaAPI;
import fr.hyriode.hystia.impl.Hystia;
import redis.clients.jedis.Jedis;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 15:47
 */
public abstract class CHyriAPIImpl extends HyriAPI {

    protected IHyriAPIConfig configuration;

    protected HyriRedisConnection redisConnection;
    protected HyriRedisProcessor redisProcessor;

    protected HyriMongoDB mongoDB;

    protected IHyriEventBus eventBus;
    protected HyriPubSub pubSub;
    protected HyriScheduler scheduler;

    protected HyggdrasilManager hyggdrasilManager;
    protected IHystiaAPI hystiaAPI;
    protected HyreosAPI hyreosAPI;

    protected HyriNetworkManager networkManager;
    protected HyriProxyManager proxyManager;
    protected HyriServerManager serverManager;
    protected LobbyAPI lobbyAPI;
    protected WorldGenerationAPI worldGenerationAPI;
    protected HyriQueueManager queueManager;
    protected HyriGameManager gameManager;

    protected CHyriPlayerManager playerManager;
    protected IHyriSettingsManager playerSettingsManager;
    protected HyriLanguageManager languageManager;

    protected IHyriBoosterManager boosterManager;
    protected IHyriMoneyManager moneyManager;

    protected IHyriPartyManager partyManager;
    protected IHyriFriendManager friendManager;

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

    protected void init(HyggEnv environment) {
        this.scheduler = new HyriScheduler();

        // Databases connections
        this.redisConnection = new HyriRedisConnection(this);
        this.redisProcessor = new HyriRedisProcessor();
        this.mongoDB = new HyriMongoDB();
        this.mongoDB.startConnection();

        // Internal systems
        this.eventBus = new HyriEventBus("default");
        this.pubSub = new HyriPubSub();
        this.hystiaAPI = new Hystia(this.mongoDB.getClient());
        this.hyreosAPI = new HyreosAPI(this.pubSub.getRedisConnection().getPool());
        this.hyreosAPI.start();

        // Hyggdrasil and servers/proxies management
        this.hyggdrasilManager = new HyggdrasilManager(environment);
        this.hyggdrasilManager.start();
        this.proxyManager = new HyriProxyManager();
        this.serverManager = new HyriServerManager();
        this.lobbyAPI = new LobbyAPI();
        this.worldGenerationAPI = new WorldGenerationAPI();

        // Managers initialization
        this.networkManager = new HyriNetworkManager();
        this.queueManager = new HyriQueueManager();
        this.gameManager = new HyriGameManager();
        this.playerManager = new CHyriPlayerManager();
        this.playerSettingsManager = new HyriPlayerSettingsManager();
        this.languageManager = new HyriLanguageManager();
        this.boosterManager = new HyriBoosterManager();
        this.moneyManager = new HyriMoneyManager();
        this.partyManager = new HyriPartyManager();
        this.friendManager = new HyriFriendManager();
        this.chatChannelManager = new HyriChatChannelManager();
        this.leaderboardProvider = new HyriLeaderboardProvider();
        this.hostManager = new HostManager();
        this.hostConfigManager = new HostConfigManager();
        this.lootboxManager = new HyriLootboxManager();
    }

    protected void postInit() {
        this.languageManager.registerAdapter(IHyriPlayer.class, (message, account) -> message.getValue(account.getSettings().getLanguage()));
        this.languageManager.registerAdapter(UUID.class, (message, uuid) -> {
            final HyriLanguage cachedLanguage = this.languageManager.getCache(uuid);

            return cachedLanguage != null ? message.getValue(cachedLanguage) : message.getValue(IHyriPlayer.get(uuid));
        });
    }

    public void stop() {
        if (this.redisConnection.isConnected()) {
            this.pubSub.stop();
            this.redisProcessor.stop();
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
    public Jedis getRedisResource() {
        return this.redisConnection.getResource();
    }

    @Override
    public HyriRedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    @Override
    public HyriRedisProcessor getRedisProcessor() {
        return this.redisProcessor;
    }

    @Override
    public IMongoDB getMongoDB() {
        return this.mongoDB;
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
    public IHystiaAPI getHystiaAPI() {
        return this.hystiaAPI;
    }

    @Override
    public HyreosAPI getHyreosAPI() {
        return this.hyreosAPI;
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
    public WorldGenerationAPI getWorldGenerationAPI() {
        return this.worldGenerationAPI;
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
    public CHyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public IHyriSettingsManager getPlayerSettingsManager() {
        return this.playerSettingsManager;
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
    public IHyriFriendManager getFriendManager() {
        return this.friendManager;
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

}
