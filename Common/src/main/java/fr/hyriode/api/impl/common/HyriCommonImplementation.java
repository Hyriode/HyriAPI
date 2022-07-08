package fr.hyriode.api.impl.common;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.event.HyriEventBus;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.booster.HyriBoosterManager;
import fr.hyriode.api.impl.common.chat.HyriChatChannelManager;
import fr.hyriode.api.impl.common.friend.HyriFriendManager;
import fr.hyriode.api.impl.common.game.HyriGameManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
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
import fr.hyriode.api.impl.common.server.HyriCServerManager;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettingsManager;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.mongodb.IHyriMongoDB;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.settings.IHyriPlayerSettingsManager;
import fr.hyriode.hystia.api.IHystiaAPI;
import redis.clients.jedis.Jedis;

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

    protected final HyriRedisConnection redisConnection;
    protected final HyriRedisProcessor redisProcessor;

    protected final HyriMongoDB mongoDB;

    protected final IHyriEventBus eventBus;
    protected final HyriPubSub pubSub;

    protected final HyggdrasilManager hyggdrasilManager;

    protected final HyriNetworkManager networkManager;
    protected final HyriCServerManager serverManager;
    protected final HyriProxyManager proxyManager;
    protected final HyriQueueManager queueManager;
    protected final HyriGameManager gameManager;

    protected HyriCPlayerManager playerManager;
    protected final IHyriPlayerSettingsManager playerSettingsManager;

    protected final IHyriBoosterManager boosterManager;
    protected final IHyriMoneyManager moneyManager;

    protected final IHyriPartyManager partyManager;
    protected final IHyriFriendManager friendManager;

    protected final IHyriChatChannelManager chatChannelManager;

    protected final IHyriLeaderboardProvider leaderboardProvider;

    private static BiConsumer<Level, String> logging;

    public HyriCommonImplementation(IHyriAPIConfig configuration, Logger logger, BiConsumer<Level, String> logging) {
        this.configuration = configuration;
        HyriCommonImplementation.logging = logging;

        for (String line : HEADER_LINES) {
            log(line);
        }

        log("Registered " + this.getClass().getName() + " as an implementation of " + NAME + ".");

        this.hyggdrasilManager = new HyggdrasilManager(logger, () -> this);
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
        this.networkManager = new HyriNetworkManager();
        this.queueManager = new HyriQueueManager(this.hyggdrasilManager);
        this.gameManager = new HyriGameManager();
        this.playerSettingsManager = new HyriPlayerSettingsManager();
        this.boosterManager = new HyriBoosterManager();
        this.moneyManager = new HyriMoneyManager();
        this.partyManager = new HyriPartyManager();
        this.friendManager = new HyriFriendManager(this);
        this.chatChannelManager = new HyriChatChannelManager();
        this.leaderboardProvider = new HyriLeaderboardProvider();
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
        this.mongoDB.stopConnection();
    }

    @Override
    public IHystiaAPI getHystiaAPI() {
        return null;
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
        return null;
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
    public HyggdrasilManager getHyggdrasilManager() {
        return this.hyggdrasilManager;
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

}
