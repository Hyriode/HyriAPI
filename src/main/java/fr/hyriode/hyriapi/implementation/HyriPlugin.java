package fr.hyriode.hyriapi.implementation;

import fr.hyriode.hyggdrasilconnector.api.ServerState;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.implementation.configuration.Configuration;
import fr.hyriode.hyriapi.implementation.configuration.ConfigurationManager;
import fr.hyriode.hyriapi.implementation.hyggdrasil.HyggdrasilManager;
import fr.hyriode.hyriapi.implementation.redis.RedisConnection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
        log("#=============[Welcome to HyriAPI]=============#");
        log("# HyriAPI is now starting... Please read       #");
        log("# carefully all information coming from it.    #");
        log("#==============================================#");
        log("# HyriAPI is a production of Hyriode           #");
        log("# Authors: AstFaster, Keinz_                   #");
        log("#==============================================#");

        this.configurationManager = new ConfigurationManager(this, true);

        this.redisConnection = new RedisConnection(this);

        this.hyggdrasilManager = new HyggdrasilManager(this);
        this.hyggdrasilManager.start();

        this.api = new HyriImplementation(this);

        this.api.getServer().setState(ServerState.READY);
    }

    @Override
    public void onDisable() {
        this.redisConnection.stop();

        this.hyggdrasilManager.stop();
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
