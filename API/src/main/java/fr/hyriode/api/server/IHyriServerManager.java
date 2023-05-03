package fr.hyriode.api.server;

import fr.hyriode.api.server.reconnection.IHyriReconnectionHandler;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.HyggServerCreationInfo;

import java.util.Set;
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
    Set<HyggServer> getServers();

    /**
     * Get the collection of all servers currently running with a given type
     * Example: "lobby" type given, return all servers with "lobby" as type
     *
     * @param type The type of the servers
     * @return A collection of {@link HyggServer}
     */
    Set<HyggServer> getServers(String type);

    /**
     * Get the server object from its name
     *
     * @param name The name of the server to get
     * @return A {@link HyggServer}
     */
    HyggServer getServer(String name);

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
     * @param serverInfo The information of the server to create
     * @param onCreated The {@link Consumer} to call when the server will be created
     */
    void createServer(HyggServerCreationInfo serverInfo, Consumer<HyggServer> onCreated);

    /**
     * Remove a server by giving its name
     *
     * @param serverName The name of the server to remove
     * @param onRemoved The {@link Runnable} to run when the server will be removed
     */
    void removeServer(String serverName, Runnable onRemoved);

    /**
     * Pause a server by giving its name
     *
     * @param serverName The name of the server to pause
     * @param onPause The {@link Runnable} to run when the server will be paused
     */
    void pauseServer(String serverName, Runnable onPause);

    /**
     * Unpause a server by giving its name
     *
     * @param serverName The name of the server to pause
     * @param onUnpause The {@link Runnable} to run when the server will be unpaused
     */
    void unpauseServer(String serverName, Runnable onUnpause);

    /**
     * Wait for a server to have a given state
     *
     * @param serverName The name of the server
     * @param state The state to wait for
     * @param callback The callback to fire when the server has the good state
     */
    void waitForState(String serverName, HyggServer.State state, Consumer<HyggServer> callback);

    /**
     * Wait for a server to have a given amount of players
     *
     * @param serverName The name of the server
     * @param players The amount of players to wait for
     * @param callback The callback to fire when the server has the good state
     */
    void waitForPlayers(String serverName, int players, Consumer<HyggServer> callback);

    /**
     * Get the reconnection handler instance
     *
     * @return The {@link IHyriReconnectionHandler} instance
     */
    IHyriReconnectionHandler getReconnectionHandler();

}
