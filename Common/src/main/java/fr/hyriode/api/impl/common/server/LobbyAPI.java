package fr.hyriode.api.impl.common.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.limbo.IHyriLimboManager;
import fr.hyriode.api.server.ILobbyAPI;
import fr.hyriode.api.server.event.ServerEvacuationEvent;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 22/11/2022 at 18:46
 */
public class LobbyAPI implements ILobbyAPI {

    @Override
    public HyggServer getBestLobby() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<String> lobbies = jedis.zrange(BALANCER_KEY, 0, 0);

            if (lobbies != null && lobbies.size() > 0) {
                return HyriAPI.get().getServerManager().getServer(lobbies.get(0));
            }
            return null;
        });
    }

    @Override
    public Set<HyggServer> getLobbies() {
        return HyriAPI.get().getServerManager().getServers(TYPE);
    }

    @Override
    public void sendPlayerToLobby(UUID playerId) {
        final HyggServer bestLobby = this.getBestLobby();

        if (bestLobby == null) {
            return;
        }

        HyriAPI.get().getServerManager().sendPlayerToServer(playerId, bestLobby.getName());
    }

    @Override
    public void evacuateToLobby(String serverName) {
        final HyggServer server = HyriAPI.get().getServerManager().getServer(serverName);

        if (server == null) {
            return;
        }

        final Set<HyggServer> lobbies = this.getLobbies();
        final Queue<UUID> players = new LinkedBlockingQueue<>(server.getPlayers()); // Create a queue of players to evacuate

        for (HyggServer lobby : lobbies.stream().sorted(Comparator.comparingInt(o -> o.getPlayers().size())).collect(Collectors.toList())) { // Sort lobbies by lower to greater amount of players
            if (server.getName().equals(lobby.getName()) || lobby.getState() != HyggServer.State.READY) {
                continue;
            }

            for (int i = 0; i < server.getSlots() - server.getPlayingPlayers().size(); i++) { // For-each the available slots of the lobby
                if (players.size() == 0) { // No more players to evacuate
                    return;
                }

                final UUID player = players.poll(); // Remove a player from the queue (declared as evacuated)

                // Finally, evacuate the player
                HyriAPI.get().getServerManager().sendPlayerToServer(player, lobby.getName());
            }
        }

        // Trigger evacuation event
        final ServerEvacuationEvent event = new ServerEvacuationEvent(serverName);

        HyriAPI.get().getNetworkManager().getEventBus().publish(event);
    }

}
