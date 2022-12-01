package fr.hyriode.api.impl.common.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.server.reconnection.HyriReconnectionHandler;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.api.server.join.packet.PlayerJoinPacket;
import fr.hyriode.api.server.reconnection.IHyriReconnectionHandler;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.HyggServerCreationInfo;
import fr.hyriode.hyggdrasil.api.server.HyggServersRequester;

import java.util.*;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriServerManager implements IHyriServerManager {

    private final Map<String, HyggServer> servers;

    private final IHyriReconnectionHandler reconnectionHandler;

    public HyriServerManager() {
        this.reconnectionHandler = new HyriReconnectionHandler();
        this.servers = new HashMap<>();

        if (HyriAPI.get().getConfig().withHyggdrasil()) {
            for (HyggServer server : HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getServersRequester().fetchServers()) {
                this.servers.put(server.getName(), server);
            }
        }
    }

    public void addServer(HyggServer server) {
        this.servers.put(server.getName(), server);
    }

    public void removeServer(String serverName) {
        this.servers.remove(serverName);
    }

    @Override
    public Set<HyggServer> getServers() {
        return Collections.unmodifiableSet(new HashSet<>(this.servers.values()));
    }

    @Override
    public Set<HyggServer> getServers(String type) {
        final Set<HyggServer> result = new HashSet<>();

        for (HyggServer server : this.servers.values()) {
            if (server.getType().equals(type)) {
                result.add(server);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    public HyggServer getServer(String name) {
        return this.servers.get(name);
    }

    @Override
    public void sendPlayerToServer(UUID playerUUID, String serverName) {
        HyriAPI.get().getPubSub().send(HyriChannel.JOIN, new PlayerJoinPacket(serverName, playerUUID));
    }

    @Override
    public void createServer(HyggServerCreationInfo serverInfo, Consumer<HyggServer> onCreated) {
        this.runActionOnRequester(requester -> requester.createServer(serverInfo, onCreated));
    }

    @Override
    public void removeServer(String serverName, Runnable onRemoved) {
        this.runActionOnRequester(requester -> requester.removeServer(serverName, onRemoved));
    }

    @Override
    public void waitForState(String serverName, HyggServer.State state, Consumer<HyggServer> callback) {
        this.runActionOnRequester(requester -> requester.waitForServerState(serverName, state, callback));
    }

    @Override
    public void waitForPlayers(String serverName, int players, Consumer<HyggServer> callback) {
        this.runActionOnRequester(requester -> requester.waitForServerPlayers(serverName, players, callback));
    }

    @Override
    public IHyriReconnectionHandler getReconnectionHandler() {
        return this.reconnectionHandler;
    }

    private void runActionOnRequester(Consumer<HyggServersRequester> action) {
        if (HyriAPI.get().getConfig().withHyggdrasil()) {
            final HyggServersRequester requester = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getServersRequester();

            if (requester != null && action != null) {
                action.accept(requester);
            }
        }
    }

}
