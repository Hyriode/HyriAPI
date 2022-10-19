package fr.hyriode.api.impl.common;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.event.HyriEventBus;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.host.IHostConfigManager;
import fr.hyriode.api.impl.common.booster.HyriBoosterManager;
import fr.hyriode.api.impl.common.chat.HyriChatChannelManager;
import fr.hyriode.api.impl.common.friend.HyriFriendManager;
import fr.hyriode.api.impl.common.game.HyriGameManager;
import fr.hyriode.api.impl.common.host.HostConfigManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.common.language.HyriLanguageManager;
import fr.hyriode.api.impl.common.leaderboard.HyriLeaderboardProvider;
import fr.hyriode.api.impl.common.money.HyriMoneyManager;
import fr.hyriode.api.impl.common.mongodb.HyriMongoDB;
import fr.hyriode.api.impl.common.network.HyriNetworkManager;
import fr.hyriode.api.impl.common.party.HyriPartyManager;
import fr.hyriode.api.impl.common.player.HyriCPlayerManager;
import fr.hyriode.api.impl.common.proxy.HyriProxyManager;
import fr.hyriode.api.impl.common.pubsub.HyriPubSub;
import fr.hyriode.api.impl.common.queue.HyriQueueManager;
import fr.hyriode.api.impl.common.redis.HyriRedisConnection;
import fr.hyriode.api.impl.common.redis.HyriRedisProcessor;
import fr.hyriode.api.impl.common.scheduler.HyriScheduler;
import fr.hyriode.api.impl.common.server.HyriCServerManager;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettingsManager;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.mongodb.IHyriMongoDB;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.settings.IHyriPlayerSettingsManager;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggEnvironment;
import fr.hyriode.hylios.api.HyliosAPI;
import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hystia.api.IHystiaAPI;
import fr.hyriode.hystia.impl.Hystia;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 15:47
 */
public abstract class HyriCommonImplementation extends HyriAPI {

    protected final IHyriAPIConfig configuration;

    protected HyriRedisConnection redisConnection;
    protected HyriRedisProcessor redisProcessor;

    protected HyriMongoDB mongoDB;

    protected IHyriEventBus eventBus;
    protected HyriPubSub pubSub;
    protected HyriScheduler scheduler;

    protected HyggdrasilManager hyggdrasilManager;
    protected IHystiaAPI hystiaAPI;
    protected HyliosAPI hyliosAPI;
    protected HyreosAPI hyreosAPI;

    protected HyriNetworkManager networkManager;
    protected HyriCServerManager serverManager;
    protected HyriProxyManager proxyManager;
    protected HyriQueueManager queueManager;
    protected HyriGameManager gameManager;

    protected HyriCPlayerManager playerManager;
    protected IHyriPlayerSettingsManager playerSettingsManager;
    protected HyriLanguageManager languageManager;

    protected IHyriBoosterManager boosterManager;
    protected IHyriMoneyManager moneyManager;

    protected IHyriPartyManager partyManager;
    protected IHyriFriendManager friendManager;

    protected IHyriChatChannelManager chatChannelManager;

    protected IHyriLeaderboardProvider leaderboardProvider;

    protected IHostConfigManager hostConfigManager;

    private static BiConsumer<Level, String> logging;

    public HyriCommonImplementation(IHyriAPIConfig configuration, Logger logger, BiConsumer<Level, String> logging, HyggEnvironment environment) {
        this.configuration = configuration;
        HyriCommonImplementation.logging = logging;

        for (String line : HEADER_LINES) {
            log(line);
        }

        log("Registered " + this.getClass().getName() + " as an implementation of " + NAME + ".");

        this.scheduler = new HyriScheduler();
        this.hyggdrasilManager = new HyggdrasilManager(logger, () -> this, environment);
        this.redisConnection = new HyriRedisConnection(this);
        this.proxyManager = new HyriProxyManager(this);
        this.serverManager = new HyriCServerManager(this);
        this.hyggdrasilManager.start();
        this.proxyManager.start();
        this.serverManager.start();
        this.redisProcessor = new HyriRedisProcessor();
        this.mongoDB = new HyriMongoDB();
        this.mongoDB.startConnection();
        this.eventBus = new HyriEventBus("default");
        this.pubSub = new HyriPubSub();
        this.hystiaAPI = new Hystia(this.mongoDB.getClient());
        this.hyliosAPI = new HyliosAPI();
        this.hyreosAPI = new HyreosAPI(this.pubSub.getRedisConnection().getPool());
        this.networkManager = new HyriNetworkManager();
        this.queueManager = new HyriQueueManager();
        this.gameManager = new HyriGameManager();
        this.playerManager = new HyriCPlayerManager();
        this.playerSettingsManager = new HyriPlayerSettingsManager();
        this.languageManager = new HyriLanguageManager();
        this.boosterManager = new HyriBoosterManager();
        this.moneyManager = new HyriMoneyManager();
        this.partyManager = new HyriPartyManager();
        this.friendManager = new HyriFriendManager();
        this.chatChannelManager = new HyriChatChannelManager();
        this.leaderboardProvider = new HyriLeaderboardProvider();
        this.hostConfigManager = new HostConfigManager();

        this.languageManager.registerAdapter(IHyriPlayer.class, (message, account) -> message.getValue(account.getSettings().getLanguage()));
        this.languageManager.registerAdapter(UUID.class, (message, uuid) -> {
            final HyriLanguage cachedLanguage = this.languageManager.getCachedPlayerLanguage(uuid);

            return cachedLanguage != null ? message.getValue(cachedLanguage) : message.getValue(IHyriPlayer.get(uuid));
        });
    }

    public static void log(Level level, String message) {
        logging.accept(level, message);
    }

    public static void log(String message) {
        log(Level.INFO, message);
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
    public IHyriMongoDB getMongoDB() {
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
    public HyliosAPI getHyliosAPI() {
        return this.hyliosAPI;
    }

    @Override
    public HyreosAPI getHyreosAPI() {
        return null;
    }

    @Override
    public HyriNetworkManager getNetworkManager() {
        return this.networkManager;
    }

    @Override
    public HyriCServerManager getServerManager() {
        return this.serverManager;
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
    public HyriCPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public IHyriPlayerSettingsManager getPlayerSettingsManager() {
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
    public IHostConfigManager getHostConfigManager() {
        return this.hostConfigManager;
    }

}
