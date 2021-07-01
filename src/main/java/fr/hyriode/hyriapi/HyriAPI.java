package fr.hyriode.hyriapi;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class HyriAPI {

    private static HyriAPI instance;

    private final JavaPlugin plugin;

    public HyriAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public static HyriAPI get() {
        return instance;
    }

}
