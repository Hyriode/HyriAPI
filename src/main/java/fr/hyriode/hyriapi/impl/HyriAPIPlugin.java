package fr.hyriode.hyriapi.impl;

import com.google.gson.Gson;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.configuration.HyriAPIConfiguration;
import fr.hyriode.hyriapi.impl.pubsub.HyriPubSub;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriAPIPlugin extends JavaPlugin {

    public static final Gson GSON = new Gson();

    /** Configuration */
    private HyriAPIConfiguration configuration;

    /** API */
    private HyriAPIImplementation api;

    /** Logger */
    private static Logger logger;

    @Override
    public void onEnable() {
        logger = this.getLogger();

        log("#=================[HyriAPI]=================#");
        log("# HyriAPI is now starting...                #");
        log("#===========================================#");
        log("# HyriAPI is a production of Hyriode        #");
        log("# Authors: AstFaster, Yggdrasil80, Keinz_   #");
        log("#===========================================#");

        this.configuration = HyriAPIConfiguration.Loader.load(this);
        this.api = new HyriAPIImplementation(this);
    }

    @Override
    public void onDisable() {
        if (this.api.getRedisConnection().isConnected()) {
            ((HyriPubSub) this.api.getPubSub()).stop();
            this.api.getRedisProcessor().stop();

            this.api.getRedisConnection().stop();
        }
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public HyriAPI getAPI() {
        return this.api;
    }

    public HyriAPIConfiguration getConfiguration() {
        return this.configuration;
    }

}
