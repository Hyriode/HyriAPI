package fr.hyriode.hyriapi.implementation;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.implementation.configuration.Configuration;
import fr.hyriode.hyriapi.implementation.configuration.ConfigurationManager;
import fr.hyriode.hyriapi.implementation.redis.RedisConnection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

public class HyriPlugin extends JavaPlugin {

    /** Redis */
    private RedisConnection redisConnection;

    /** Configuration */
    private ConfigurationManager configurationManager;

    /** Thread / Execution */
    private ScheduledExecutorService executor;

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

        this.executor = Executors.newScheduledThreadPool(32);

        this.configurationManager = new ConfigurationManager(this, true);

        this.redisConnection = new RedisConnection(this);

        this.api = new HyriImplementation(this);
    }

    @Override
    public void onDisable() {
        this.redisConnection.stop();
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

    public ScheduledExecutorService getExecutor() {
        return this.executor;
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

}
