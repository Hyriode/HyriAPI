package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.impl.server.config.HyriAPIConfig;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.world.IHyriWorld;
import fr.hyriode.api.world.IHyriWorldManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

        HyriAPI.get().getServer().setState(HyggServer.State.READY);
    }

    private void loadMap() {
        if (!HyriAPI.get().getConfig().isDevEnvironment()) {
            final IHyriServer server = HyriAPI.get().getServer();

            if (server != null) {
                final IHyriWorldManager worldManager = this.api.getWorldManager();
                final String serverType = server.getType();
                final String gameType = server.getGameType();

                IHyriWorld map = null;

                if (server.getMap() != null) {
                    map = gameType != null ? worldManager.getWorld(serverType, gameType, server.getMap()) : worldManager.getWorld(serverType, server.getMap());
                }

                if (map == null) {
                    final List<IHyriWorld> maps = (gameType == null ? worldManager.getWorlds(serverType) : worldManager.getWorlds(serverType, gameType))
                            .stream()
                            .filter(IHyriWorld::isEnabled)
                            .collect(Collectors.toList());

                    if (maps.size() > 0) {
                        HyriAPI.get().log("Available maps:");

                        for (IHyriWorld value : maps) {
                            HyriAPI.get().log(" - " + value.getName());
                        }

                        map = maps.get(ThreadLocalRandom.current().nextInt(maps.size()));
                    }
                }

                if (map != null) {
                    this.api.getServer().setMap(map.getName());

                    map.load(new File("world"));
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
