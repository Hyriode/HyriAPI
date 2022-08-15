package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.impl.server.config.HyriAPIConfig;
import fr.hyriode.api.impl.server.join.HyriJoinListener;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hystia.api.world.IWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:13
 */
public class HyriAPIPlugin extends JavaPlugin {

    private IHyriAPIConfig configuration;

    private HyriAPIImplementation api;

    @Override
    public void onEnable() {
        this.configuration = HyriAPIConfig.Loader.load(this);
        this.api = new HyriAPIImplementation(this);

        this.registerListeners();

        this.loadMap();
    }

    private void loadMap() {
        if (!HyriAPI.get().getConfig().isDevEnvironment()) {
            final IHyriServer server = HyriAPI.get().getServer();

            if (server != null) {
                final IWorldManager worldManager = this.api.getHystiaAPI().getWorldManager();
                final String serverType = server.getType();
                final String subType = server.getSubType();

                String mapName = HyriAPI.get().getServer().getMap();
                if (mapName == null) {
                    final List<String> maps = subType == null ? worldManager.getWorlds(serverType) : worldManager.getWorlds(serverType, subType);

                    if (maps != null && maps.size() > 0) {
                        Collections.shuffle(maps);

                        log("Available maps:");

                        for (String map : maps) {
                            log(" - " + map);
                        }

                        mapName = maps.get(0);
                    }
                }

                if (mapName != null) {
                    this.api.getServer().setMap(mapName);

                    if (subType != null) {
                        log("Loaded '" + mapName + "' map for the server (" + serverType + "#" + subType + ").");

                        worldManager.loadWorld(new File("world"), serverType, subType, mapName);
                    }
                }
            }
        }
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

        register.accept(new HyriJoinListener(this.api, this.api.getHyggdrasilManager(), this.api.getServerManager().getJoinManager()));
    }

    public IHyriAPIConfig getConfiguration() {
        return this.configuration;
    }

    public HyriAPIImplementation getAPI() {
        return this.api;
    }

}
