package fr.hyriode.api.impl.common;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import fr.hyriode.api.cosmetic.IHyriCosmeticManager;
import fr.hyriode.api.event.HyriEventBus;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.cosmetic.HyriCosmeticManager;
import fr.hyriode.api.impl.common.friend.HyriFriendManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.common.network.HyriNetwork;
import fr.hyriode.api.impl.common.party.HyriPartyManager;
import fr.hyriode.api.impl.common.pubsub.HyriPubSub;
import fr.hyriode.api.impl.common.redis.HyriRedisConnection;
import fr.hyriode.api.impl.common.redis.HyriRedisProcessor;
import fr.hyriode.api.impl.common.server.HyriServerManager;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettingsManager;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.settings.IHyriPlayerSettingsManager;
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

    protected final IHyriAPIConfiguration configuration;

    protected final HyriRedisConnection redisConnection;
    protected final HyriRedisProcessor redisProcessor;

    protected final IHyriEventBus eventBus;

    protected final HyriPubSub pubSub;

    protected final HyriNetwork network;

    protected final HyggdrasilManager hyggdrasilManager;

    protected final HyriServerManager serverManager;

    protected final IHyriPlayerSettingsManager playerSettingsManager;

    protected final IHyriPartyManager partyManager;

    protected final IHyriFriendManager friendManager;

    protected final IHyriCosmeticManager cosmeticManager;

    private static BiConsumer<Level, String> logging;

    public HyriCommonImplementation(IHyriAPIConfiguration configuration, Logger logger, BiConsumer<Level, String> logging) {
        this.configuration = configuration;
        HyriCommonImplementation.logging = logging;

        for (String line : HEADER_LINES) {
            log(line);
        }

        log("Registered " + this.getClass().getName() + " as an implementation of " + NAME + ".");

        this.redisConnection = new HyriRedisConnection(this.configuration.getRedisConfiguration());
        this.redisProcessor = new HyriRedisProcessor();
        this.eventBus = new HyriEventBus("default");
        this.pubSub = new HyriPubSub();
        this.network = new HyriNetwork();
        this.hyggdrasilManager = new HyggdrasilManager(logger, this);
        this.serverManager = new HyriServerManager(this);
        this.playerSettingsManager = new HyriPlayerSettingsManager();
        this.partyManager = new HyriPartyManager();
        this.friendManager = new HyriFriendManager();
        this.cosmeticManager = new HyriCosmeticManager();
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
            this.hyggdrasilManager.stop();
        }
    }

    @Override
    public IHyriAPIConfiguration getConfiguration() {
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
    public IHyriEventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public HyriPubSub getPubSub() {
        return this.pubSub;
    }

    @Override
    public HyriNetwork getNetwork() {
        return this.network;
    }

    public HyggdrasilManager getHyggdrasilManager() {
        return this.hyggdrasilManager;
    }

    @Override
    public HyriServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public IHyriPlayerSettingsManager getPlayerSettingsManager() {
        return this.playerSettingsManager;
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
    public IHyriCosmeticManager getCosmeticManager() {
        return this.cosmeticManager;
    }

}
