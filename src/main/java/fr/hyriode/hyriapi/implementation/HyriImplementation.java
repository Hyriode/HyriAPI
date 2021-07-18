package fr.hyriode.hyriapi.implementation;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.implementation.configuration.Configuration;
import fr.hyriode.hyriapi.implementation.configuration.ConfigurationManager;
import fr.hyriode.hyriapi.server.Server;
import fr.hyriode.hyriapi.tools.bossbar.BossBarHandler;
import fr.hyriode.hyriapi.tools.inventory.InventoryHandler;
import fr.hyriode.hyriapi.tools.item.ItemHandler;
import fr.hyriode.hyriapi.tools.scoreboard.team.ScoreboardTeamHandler;
import redis.clients.jedis.Jedis;

public class HyriImplementation extends HyriAPI {

    /** Configuration */
    private final ConfigurationManager configurationManager;

    /** Server */
    private final Server server;

    /** Plugin */
    private final HyriPlugin plugin;

    public HyriImplementation(HyriPlugin plugin) {
        this.plugin = plugin;

        this.server = this.plugin.getHyggdrasilManager().getServer();

        this.configurationManager = new ConfigurationManager(this.plugin, true);

        new ItemHandler(this.plugin);
        new InventoryHandler(this.plugin);
        new BossBarHandler(this.plugin);
        new ScoreboardTeamHandler(this.plugin);
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public Configuration getConfiguration() {
        return this.configurationManager.getConfiguration();
    }

    @Override
    public Jedis getJedisResource() {
        return this.plugin.getRedisConnection().getResource();
    }

}
