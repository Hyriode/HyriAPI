package fr.hyriode.hyriapi.implementation;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.implementation.api.server.ServerManager;
import fr.hyriode.hyriapi.server.IServerManager;
import fr.hyriode.hyriapi.server.AbstractServer;
import fr.hyriode.hyriapi.tools.bossbar.BossBarHandler;
import fr.hyriode.hyriapi.tools.inventory.InventoryHandler;
import fr.hyriode.hyriapi.tools.item.ItemHandler;
import fr.hyriode.hyriapi.tools.scoreboard.team.ScoreboardTeamHandler;
import redis.clients.jedis.Jedis;

public class HyriImplementation extends HyriAPI {

    /** Server */
    private final IServerManager serverManager;
    private final AbstractServer server;

    /** Plugin */
    private final HyriPlugin plugin;

    public HyriImplementation(HyriPlugin plugin) {
        this.plugin = plugin;

        this.server = this.plugin.getHyggdrasilManager().getServer();
        this.serverManager = new ServerManager(this.plugin);

        new ItemHandler(this.plugin);
        new InventoryHandler(this.plugin);
        new BossBarHandler(this.plugin);
        new ScoreboardTeamHandler(this.plugin);
    }

    @Override
    public AbstractServer getServer() {
        return this.server;
    }

    @Override
    public Jedis getJedisResource() {
        return this.plugin.getRedisConnection().getResource();
    }

    @Override
    public IServerManager getServerManager() {
        return this.serverManager;
    }

}
