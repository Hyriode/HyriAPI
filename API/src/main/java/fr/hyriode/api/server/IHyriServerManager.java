package fr.hyriode.api.server;

import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.HyggServerState;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 20:17
 */
public interface IHyriServerManager {

    /**
     * Get the collection of all servers currently running
     *
     * @return A list of {@link HyggServer}
     */
    Collection<HyggServer> getServers();

    /**
     * Get the collection of all servers currently running with a given type
     * Example: "lobby" type given, return all servers with "lobby" as type
     *
     * @param type The type of the servers
     * @return A collection of {@link HyggServer}
     */
    Collection<HyggServer> getServers(String type);

    /**
     * Get the server object from its name
     *
     * @param name The name of the server to get
     * @return A {@link HyggServer}
     */
    HyggServer getServer(String name);

    /**
     * Get the actual best lobby server
     *
     * @return A {@link HyggServer}
     */
    HyggServer getLobby();

    /**
     * Get all the lobbies servers
     *
     * @return A list of {@link HyggServer}
     */
    List<HyggServer> getLobbies();

    /**
     * Send a player to a lobby
     *
     * @param playerUUID The {@link UUID} of a given player
     */
    void sendPlayerToLobby(UUID playerUUID);

    /**
     * Send a player to a given server
     *
     * @param playerUUID The {@link UUID} of a given player
     * @param serverName A {@link String} which represents the name of a server
     */
    void sendPlayerToServer(UUID playerUUID, String serverName);

    /**
     * Create a server with a given type
     *
     * @param serverType The type of the server to create
     * @param onCreated The {@link Consumer} to call when the server will be created
     */
    void createServer(String serverType, Consumer<HyggServer> onCreated);

    /**
     * Remove a server by giving its name
     *
     * @param serverName The name of the server to remove
     * @param onRemoved The {@link Runnable} to run when the server will be removed
     */
    void removeServer(String serverName, Runnable onRemoved);

    /**
     * Wait for a server to have a given state
     *
     * @param serverName The name of the server
     * @param state The state to wait for
     * @param callback The callback to fire when the server has the good state
     */
    void waitForState(String serverName, HyggServerState state, Consumer<HyggServer> callback);

    /**
     * Wait for a server to have a given amount of players
     *
     * @param serverName The name of the server
     * @param players The amount of players to wait for
     * @param callback The callback to fire when the server has the good state
     */
    void waitForPlayers(String serverName, int players, Consumer<HyggServer> callback);

}
