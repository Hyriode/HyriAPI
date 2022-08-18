package fr.hyriode.api.impl.common.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.packet.BroadcastPacket;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.server.reconnection.HyriReconnectionHandler;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.model.HyriEvacuateServerPacket;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.api.server.join.IHyriJoinManager;
import fr.hyriode.api.server.join.packet.HyriPartyJoinPacket;
import fr.hyriode.api.server.join.packet.HyriPlayerJoinPacket;
import fr.hyriode.api.server.join.packet.HyriPlayerSpectatePacket;
import fr.hyriode.api.server.reconnection.IHyriReconnectionHandler;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.HyggServerRequest;
import fr.hyriode.hyggdrasil.api.server.HyggServerRequester;
import fr.hyriode.hyggdrasil.api.server.HyggServerState;
import fr.hyriode.hylios.api.lobby.LobbyAPI;

import java.util.*;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriCServerManager implements IHyriServerManager {

    private final Map<String, HyggServer> servers;

    private final IHyriReconnectionHandler reconnectionHandler;

    private final HyriCommonImplementation implementation;

    public HyriCServerManager(HyriCommonImplementation implementation) {
        this.implementation = implementation;
        this.reconnectionHandler = new HyriReconnectionHandler();
        this.servers = new HashMap<>();
    }

    public void start() {
        this.runActionOnRequester(requester -> requester.fetchServers(null, servers -> servers.forEach(server -> this.servers.put(server.getName(), server))));
    }

    public void addServer(HyggServer server) {
        this.servers.put(server.getName(), server);
    }

    public void removeServer(String serverName) {
        this.servers.remove(serverName);
    }

    @Override
    public void broadcastMessage(UUID sender, String component) {
        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new BroadcastPacket(component, sender));
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
        for (HyggServer server : this.servers.values()) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    @Override
    public HyggServer getLobby() {
        final String bestLobby = HyriAPI.get().getHyliosAPI().getLobbyAPI().getBestLobby();

        return bestLobby != null ? this.getServer(bestLobby) : null;
    }

    @Override
    public List<HyggServer> getLobbies() {
        return new ArrayList<>(this.getServers(LobbyAPI.TYPE));
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
        HyriAPI.get().getPubSub().send(HyriChannel.JOIN, new HyriPlayerJoinPacket(serverName, playerUUID));
    }

    @Override
    public void sendSpectatorToServer(UUID playerId, String serverName) {
        HyriAPI.get().getPubSub().send(HyriChannel.JOIN, new HyriPlayerSpectatePacket(serverName, playerId));
    }

    @Override
    public void sendPartyToLobby(UUID partyId) {
        final HyggServer lobby = this.getLobby();

        if (lobby != null) {
            this.sendPartyToServer(partyId, lobby.getName());
        }
    }

    @Override
    public void sendPartyToServer(UUID partyId, String serverName) {
        HyriAPI.get().getPubSub().send(HyriChannel.JOIN, new HyriPartyJoinPacket(serverName, partyId));
    }

    @Override
    public void evacuateServer(String from, String destination) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new HyriEvacuateServerPacket(from, destination));
    }

    @Override
    public void createServer(HyggServerRequest serverRequest, Consumer<HyggServer> onCreated) {
        this.runActionOnRequester(requester -> requester.createServer(serverRequest, onCreated));
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

    @Override
    public IHyriJoinManager getJoinManager() {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Override
    public IHyriReconnectionHandler getReconnectionHandler() {
        return this.reconnectionHandler;
    }

    private void runActionOnRequester(Consumer<HyggServerRequester> action) {
        if (HyriAPI.get().getConfig().withHyggdrasil()) {
            final HyggServerRequester requester = this.implementation.getHyggdrasilManager().getHyggdrasilAPI().getServerRequester();

            if (requester != null && action != null) {
                action.accept(requester);
            }
        }
    }

}
