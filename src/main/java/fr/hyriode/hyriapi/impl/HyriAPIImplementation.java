package fr.hyriode.hyriapi.impl;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.cosmetic.IHyriCosmeticManager;
import fr.hyriode.hyriapi.impl.cosmetic.HyriCosmeticManager;
import fr.hyriode.hyriapi.impl.money.HyriMoneyManager;
import fr.hyriode.hyriapi.impl.party.HyriPartyManager;
import fr.hyriode.hyriapi.impl.player.HyriPlayerManager;
import fr.hyriode.hyriapi.impl.pubsub.HyriPubSub;
import fr.hyriode.hyriapi.impl.rank.HyriRankManager;
import fr.hyriode.hyriapi.impl.redis.HyriRedisConnection;
import fr.hyriode.hyriapi.impl.redis.HyriRedisProcessor;
import fr.hyriode.hyriapi.impl.server.HyriServer;
import fr.hyriode.hyriapi.impl.server.HyriServerManager;
import fr.hyriode.hyriapi.impl.settings.HyriPlayerSettingsManager;
import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.party.IHyriPartyManager;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.pubsub.IHyriPubSub;
import fr.hyriode.hyriapi.rank.IHyriRankManager;
import fr.hyriode.hyriapi.server.IHyriServer;
import fr.hyriode.hyriapi.server.IHyriServerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettingsManager;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriAPIImplementation extends HyriAPI {

    /** Cosmetic */
    private final IHyriCosmeticManager cosmeticManager;

    /*** Rank */
    private final IHyriRankManager rankManager;

    /** Party */
    private final IHyriPartyManager partyManager;

    /** Money */
    private final IHyriMoneyManager moneyManager;

    /** Player Settings */
    private final IHyriPlayerSettingsManager playerSettingsManager;

    /** Player */
    private final IHyriPlayerManager playerManager;

    /** Server */
    private final IHyriServer server;
    private final IHyriServerManager serverManager;

    /** PubSub */
    private final IHyriPubSub pubSub;

    /** Redis */
    private final HyriRedisConnection redisConnection;
    private final HyriRedisProcessor redisProcessor;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        this.redisConnection = new HyriRedisConnection(plugin);
        this.redisProcessor = new HyriRedisProcessor();
        this.pubSub = new HyriPubSub();
        this.server = new HyriServer("server-" + UUID.randomUUID().toString().split("-")[0], "server", System.currentTimeMillis(), 0);
        this.serverManager = new HyriServerManager(plugin);
        this.playerManager = new HyriPlayerManager(plugin);
        this.playerSettingsManager = new HyriPlayerSettingsManager(plugin);
        this.moneyManager = new HyriMoneyManager(plugin);
        this.partyManager = new HyriPartyManager(plugin);
        this.rankManager = new HyriRankManager();
        this.cosmeticManager = new HyriCosmeticManager(plugin);
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
    public IHyriPubSub getPubSub() {
        return this.pubSub;
    }

    @Override
    public IHyriServer getServer() {
        return this.server;
    }

    @Override
    public IHyriServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public IHyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public IHyriPlayerSettingsManager getPlayerSettingsManager() {
        return this.playerSettingsManager;
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
    public IHyriRankManager getRankManager() {
        return this.rankManager;
    }

    @Override
    public IHyriCosmeticManager getCosmeticManager() {
        return this.cosmeticManager;
    }

}
