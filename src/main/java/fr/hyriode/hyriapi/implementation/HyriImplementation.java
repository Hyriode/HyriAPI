package fr.hyriode.hyriapi.implementation;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.implementation.configuration.Configuration;
import fr.hyriode.hyriapi.implementation.configuration.ConfigurationManager;
import fr.hyriode.hyriapi.tools.bossbar.BossBarHandler;
import fr.hyriode.hyriapi.tools.inventory.InventoryHandler;
import fr.hyriode.hyriapi.tools.item.ItemHandler;
import fr.hyriode.hyriapi.tools.npc.NPCHandler;
import fr.hyriode.hyriapi.tools.npc.NPCManager;
import fr.hyriode.hyriapi.tools.scoreboard.team.ScoreboardTeamHandler;
import redis.clients.jedis.Jedis;

public class HyriImplementation extends HyriAPI {

    /** NPC */
    private final NPCManager npcManager;

    /** Configuration */
    private final ConfigurationManager configurationManager;

    private final HyriPlugin plugin;

    public HyriImplementation(HyriPlugin plugin) {
        this.plugin = plugin;

        this.configurationManager = new ConfigurationManager(this.plugin, true);

        this.npcManager = new NPCManager(this.plugin);

        new ItemHandler(this.plugin);
        new InventoryHandler(this.plugin);
        new NPCHandler(this.plugin);
        new BossBarHandler(this.plugin);
        new ScoreboardTeamHandler(this.plugin);
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

    @Override
    public NPCManager getNPCManager() {
        return this.npcManager;
    }

}
