package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.impl.server.config.HyriAPIConfig;
import fr.hyriode.api.impl.server.join.JoinListener;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hystia.api.world.IWorldManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:13
 */
public class HyriAPIPlugin extends JavaPlugin {

    private IHyriAPIConfig configuration;

    private SHyriAPIImpl api;

    @Override
    public void onEnable() {
        this.configuration = HyriAPIConfig.Loader.load(this);
        this.api = new SHyriAPIImpl(this);

        this.loadMap();
    }

    private void loadMap() {
        if (!HyriAPI.get().getConfig().isDevEnvironment()) {
            final IHyriServer server = HyriAPI.get().getServer();

            if (server != null) {
                final IWorldManager worldManager = this.api.getHystiaAPI().getWorldManager();
                final String serverType = server.getType();
                final String gameType = server.getGameType();

                String mapName = HyriAPI.get().getServer().getMap();
                if (mapName == null) {
                    final List<String> maps = gameType == null ? worldManager.getWorlds(serverType) : worldManager.getWorlds(serverType, gameType);

                    if (maps != null && maps.size() > 0) {
                        Collections.shuffle(maps);

                        HyriAPI.get().log("Available maps:");

                        for (String map : maps) {
                            HyriAPI.get().log(" - " + map);
                        }

                        mapName = maps.get(0);
                    }
                }

                if (mapName != null) {
                    this.api.getServer().setMap(mapName);

                    if (gameType != null) {
                        HyriAPI.get().log("Loaded '" + mapName + "' map for the server (" + serverType + "#" + gameType + ").");

                        worldManager.loadWorld(new File("world"), serverType, gameType, mapName);
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.api.stop();

        HyriAPI.get().log(HyriAPI.NAME + " is now disabled. See you soon!");
    }

    public IHyriAPIConfig getConfiguration() {
        return this.configuration;
    }

    public SHyriAPIImpl getAPI() {
        return this.api;
    }

}
