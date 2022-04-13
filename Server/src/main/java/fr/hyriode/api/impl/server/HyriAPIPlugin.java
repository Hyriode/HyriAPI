package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import fr.hyriode.api.impl.server.configuration.HyriAPIConfiguration;
import fr.hyriode.api.impl.server.listener.HyriPlayerListener;
import fr.hyriode.api.server.IHyriServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:13
 */
public class HyriAPIPlugin extends JavaPlugin {

    private IHyriAPIConfiguration configuration;

    private HyriAPIImplementation api;

    @Override
    public void onEnable() {
        this.configuration = HyriAPIConfiguration.Loader.load(this);
        this.api = new HyriAPIImplementation(this);

        this.registerListeners();

        HyriAPI.get().getServer().setState(IHyriServer.State.READY);
    }

    @Override
    public void onDisable() {
        this.api.stop();

        log(HyriAPI.NAME + " is now disabled. See you soon!");
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

    private void registerListeners() {
        final Consumer<Listener> register = listener -> this.getServer().getPluginManager().registerEvents(listener, this);

        register.accept(new HyriPlayerListener(this.api.getHyggdrasilManager(), this.api.getHydrionManager()));
    }

    public IHyriAPIConfiguration getConfiguration() {
        return this.configuration;
    }

    public HyriAPIImplementation getAPI() {
        return this.api;
    }

}
