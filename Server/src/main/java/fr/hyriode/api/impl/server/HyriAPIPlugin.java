package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import fr.hyriode.api.impl.server.configuration.HyriAPIConfiguration;
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
import java.util.concurrent.ExecutionException;
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

        this.loadMap();

        HyriAPI.get().getServer().setState(IHyriServer.State.READY);
    }

    private void loadMap() {
        if (this.api.getHydrionManager().isEnabled()) {
            final IHyriServer server = HyriAPI.get().getServer();

            if (server != null) {
                final IWorldManager worldManager = this.api.getHystiaAPI().getWorldManager();
                final String gameType = server.getGameType();
                final String serverType = server.getType();

                String mapName = HyriAPI.get().getServer().getMap();
                if (mapName == null) {
                    try {
                        final List<String> maps = gameType == null ? worldManager.getWorlds(serverType).get() : worldManager.getWorlds(serverType, gameType).get();

                        Collections.shuffle(maps);

                        mapName = maps.get(0);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                if (mapName != null) {
                    if (gameType != null) {
                        worldManager.loadWorld(new File("./world/"), serverType, gameType, mapName);
                    } else {
                        worldManager.loadWorld(new File("./world/"), serverType, mapName);
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

        register.accept(new HyriJoinListener(this.api.getHyggdrasilManager(), this.api.getHydrionManager(), this.api.getServerManager().getJoinManager()));
    }

    public IHyriAPIConfiguration getConfiguration() {
        return this.configuration;
    }

    public HyriAPIImplementation getAPI() {
        return this.api;
    }

}
