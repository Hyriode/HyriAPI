package fr.hyriode.api.impl.common.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.model.HyriSendPlayerPacket;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.HyggServerRequester;
import fr.hyriode.hyggdrasil.api.server.HyggServerState;

import java.util.*;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriServerManager implements IHyriServerManager {

    private static final String LOBBY_TYPE = "lobby";

    private final Map<String, HyggServer> servers;

    private final HyriCommonImplementation implementation;

    public HyriServerManager(HyriCommonImplementation implementation) {
        this.implementation = implementation;
        this.servers = new HashMap<>();
    }

    public void addServer(HyggServer server) {
        this.servers.put(server.getName(), server);
    }

    public void removeServer(String serverName) {
        this.servers.remove(serverName);
    }

    @Override
    public Collection<HyggServer> getServers() {
        return this.servers.values();
    }

    @Override
    public Collection<HyggServer> getServers(String type) {
        final List<HyggServer> result = new ArrayList<>();

        for (HyggServer server : this.servers.values()) {
            if (server.getType().equals(type)) {
                result.add(server);
            }
        }
        return result;
    }

    @Override
    public HyggServer getServer(String name) {
        for (HyggServer server : this.getServers()) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    @Override
    public HyggServer getLobby() {
        HyggServer result = null;
        for (HyggServer lobby : this.getLobbies()) {
            if (lobby.getState() == HyggServerState.READY) {
                if (result == null) {
                    result = lobby;
                } else if (lobby.getPlayers() < result.getPlayers()) {
                    result = lobby;
                }
            }
        }
        return result;
    }

    @Override
    public List<HyggServer> getLobbies() {
        final List<HyggServer> lobbies = new ArrayList<>();

        for (HyggServer server : this.getServers()) {
            if (server.getType().equals(LOBBY_TYPE)) {
                lobbies.add(server);
            }
        }
        return lobbies;
    }

    @Override
    public void sendPlayerToLobby(UUID playerUUID) {
        final HyggServer lobby = this.getLobby();

        if (lobby != null) {
            this.sendPlayerToServer(playerUUID, lobby.getName());
        }
    }

    @Override
    public void sendPlayerToServer(UUID playerUUID, String serverName) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new HyriSendPlayerPacket(playerUUID, serverName));
    }

    @Override
    public void createServer(String serverType, Consumer<HyggServer> onCreated) {
        this.runActionOnRequester(requester -> requester.createServer(serverType, onCreated));
    }

    @Override
    public void removeServer(String serverName, Runnable onRemoved) {
        this.runActionOnRequester(requester -> requester.removeServer(serverName, onRemoved));
    }

    @Override
    public void waitForState(String serverName, HyggServerState state, Consumer<HyggServer> callback) {
        this.runActionOnRequester(requester -> requester.waitForServerState(serverName, state, callback));
    }

    @Override
    public void waitForPlayers(String serverName, int players, Consumer<HyggServer> callback) {
        this.runActionOnRequester(requester -> requester.waitForServerPlayers(serverName, players, callback));
    }

    private void runActionOnRequester(Consumer<HyggServerRequester> action) {
        if (HyriAPI.get().getConfiguration().withHyggdrasil()) {
            final HyggServerRequester requester = this.implementation.getHyggdrasilManager().getHyggdrasilAPI().getServerRequester();

            if (requester != null) {
                action.accept(requester);
            }
        }
    }

}
