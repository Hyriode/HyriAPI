package fr.hyriode.hyriapi.impl;

import com.google.gson.Gson;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.configuration.HyriAPIConfiguration;
import fr.hyriode.hyriapi.impl.pubsub.HyriPubSub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

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

    @Override
    public void onEnable() {
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
        String prefix = ChatColor.DARK_AQUA + "[" + HyriAPI.NAME + "] ";

        if (level == Level.SEVERE) {
            prefix += ChatColor.RED;
        } else if (level == Level.WARNING) {
            prefix += ChatColor.YELLOW;
        } else {
            prefix += ChatColor.RESET;
        }

        Bukkit.getConsoleSender().sendMessage(prefix + message);
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
