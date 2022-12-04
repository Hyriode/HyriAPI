package fr.hyriode.api.server;

import fr.hyriode.hyggdrasil.api.server.HyggServer;

import java.util.Set;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 21/11/2022 at 17:06
 */
public interface ILobbyAPI {

    /** The redis key used to balance lobby */
    String BALANCER_KEY = "lobby-balancer";
    /** The type of the lobby servers */
    String TYPE = "lobby";
    /** The maximum amount of players that can handle a lobby. */
    int MAX_PLAYERS = 80;

    /**
     * Get the current best lobby in terms of connected players.
     *
     * @return The best {@link HyggServer} object
     */
    HyggServer getBestLobby();

    /**
     * Get all the lobbies available on the network.
     *
     * @return A list of {@link HyggServer}
     */
    Set<HyggServer> getLobbies();

    /**
     * Send a player to lobby.
     *
     * @param playerId The {@link UUID} of the player to send
     */
    void sendPlayerToLobby(UUID playerId);

    /**
     * Evacuate all players to lobbies
     *
     * @param server The name of the server to evacuate
     */
    void evacuateToLobby(String server);

}
