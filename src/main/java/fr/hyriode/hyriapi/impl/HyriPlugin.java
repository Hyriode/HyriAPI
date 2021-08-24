package fr.hyriode.hyriapi.impl;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.api.rank.EHyriRankImpl;
import fr.hyriode.hyriapi.impl.configuration.Configuration;
import fr.hyriode.hyriapi.impl.configuration.ConfigurationManager;
import fr.hyriode.hyriapi.impl.hyggdrasil.HyggdrasilManager;
import fr.hyriode.hyriapi.impl.redis.RedisConnection;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class HyriPlugin extends JavaPlugin {

    /** Hyggdrasil */
    private HyggdrasilManager hyggdrasilManager;

    /** Redis */
    private RedisConnection redisConnection;

    /** Configuration */
    private ConfigurationManager configurationManager;

    /** API */
    private HyriAPI api;

    @Override
    public void onEnable() {
        this.log("#=============[Welcome to HyriAPI]=============#");
        this.log("# HyriAPI is now starting... Please read       #");
        this.log("# carefully all information coming from it.    #");
        this.log("#==============================================#");
        this.log("# HyriAPI is a production of Hyriode           #");
        this.log("# Authors: AstFaster, Keinz_                   #");
        this.log("#==============================================#");

        this.configurationManager = new ConfigurationManager(this, true);

        this.redisConnection = new RedisConnection(this);

        this.hyggdrasilManager = new HyggdrasilManager(this);
        //this.hyggdrasilManager.start();

        this.api = new HyriImplementation(this);

        //this.api.getServer().setState(ServerState.READY);
    }

    @Override
    public void onDisable() {
        this.redisConnection.stop();

        //this.hyggdrasilManager.stop();
    }

    public void log(Level level, String message) {
        this.getLogger().log(level, message);
    }

    public void log(String message) {
        this.log(Level.INFO, message);
    }


    public HyriAPI getAPI() {
        return this.api;
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public Configuration getConfiguration() {
        return this.configurationManager.getConfiguration();
    }

    public RedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    public HyggdrasilManager getHyggdrasilManager() {
        return this.hyggdrasilManager;
    }

}
