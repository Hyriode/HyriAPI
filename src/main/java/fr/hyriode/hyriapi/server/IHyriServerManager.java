package fr.hyriode.hyriapi.server;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 20:17
 */
public interface IHyriServerManager {

    /**
     * Get the list of all servers currently running
     *
     * @return - A list of {@link IHyriServer}
     */
    List<IHyriServer> getServers();

    /**
     * Get the list of all servers currently running with the given type
     * Example: "lobby" type given, return all servers with the same type
     *
     * @param type - Servers type
     * @return - A list of {@link IHyriServer}
     */
    List<IHyriServer> getServers(String type);

    /**
     * Get the actual best lobby server
     *
     * @return - A {@link IHyriServer}
     */
    IHyriServer getLobbyServer();

    /**
     * Send a player to a lobby
     *
     * @param playerUuid - The {@link UUID} of a given player
     */
    void sendPlayerToLobby(UUID playerUuid);

    /**
     * Send a player to a given server
     *
     * @param playerUuid - The {@link UUID} of a given player
     * @param serverId - A {@link String} which represents the id of a server
     */
    void sendPlayerToServer(UUID playerUuid, String serverId);

    /**
     * Create a server with a given type
     *
     * @param type - Server type
     */
    void createServer(String type);


}
