package fr.hyriode.hyriapi.impl.server;

import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import fr.hyriode.hyriapi.server.IHyriServer;
import fr.hyriode.hyriapi.server.IHyriServerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriServerManager implements IHyriServerManager {

    private final HyriAPIPlugin plugin;

    public HyriServerManager(HyriAPIPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<IHyriServer> getServers() {
        return new ArrayList<>();
    }

    @Override
    public List<IHyriServer> getServers(String prefix) {
        return new ArrayList<>();
    }

    @Override
    public IHyriServer getLobbyServer() {
        // TODO Get from redis with Hub Balancer
        return null;
    }

    @Override
    public void sendPlayerToLobby(UUID playerUuid) {

    }

    @Override
    public void sendPlayerToServer(UUID playerUuid, String server) {

    }

    @Override
    public void createServer(String type) {

    }

}
