package fr.hyriode.hyriapi.impl;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.api.money.HyriMoneyManager;
import fr.hyriode.hyriapi.impl.api.party.HyriPartyManager;
import fr.hyriode.hyriapi.impl.api.player.HyriPlayerManager;
import fr.hyriode.hyriapi.impl.api.rank.HyriRankManager;
import fr.hyriode.hyriapi.impl.api.server.HyriServerManager;
import fr.hyriode.hyriapi.impl.api.settings.HyriPlayerSettingsManager;
import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.party.IHyriPartyManager;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.rank.IHyriRankManager;
import fr.hyriode.hyriapi.server.IHyriServer;
import fr.hyriode.hyriapi.server.IHyriServerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettingsManager;
import fr.hyriode.tools.bossbar.BossBarHandler;
import fr.hyriode.tools.inventory.InventoryHandler;
import fr.hyriode.tools.item.ItemHandler;
import fr.hyriode.tools.scoreboard.team.ScoreboardTeamHandler;
import redis.clients.jedis.Jedis;

public class HyriImplementation extends HyriAPI {

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
    private final IHyriServerManager serverManager;

    /** Plugin */
    private final HyriPlugin plugin;

    public HyriImplementation(HyriPlugin plugin) {
        this.plugin = plugin;

        this.serverManager = new HyriServerManager(this.plugin);
        this.playerManager = new HyriPlayerManager(this.plugin);
        this.playerSettingsManager = new HyriPlayerSettingsManager(this.plugin);
        this.moneyManager = new HyriMoneyManager(this.plugin);
        this.partyManager = new HyriPartyManager(this.plugin);
        this.rankManager = new HyriRankManager();

        new ItemHandler(this.plugin);
        new InventoryHandler(this.plugin);
        new BossBarHandler(this.plugin);
        new ScoreboardTeamHandler(this.plugin);
    }

    @Override
    public Jedis getJedisResource() {
        return this.plugin.getRedisConnection().getResource();
    }

    @Override
    public IHyriServer getServer() {
        return this.plugin.getHyggdrasilManager().getServer();
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

}
