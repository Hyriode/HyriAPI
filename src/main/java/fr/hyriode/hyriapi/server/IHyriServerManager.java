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
     * @return - A list of {@link String} (a {@link String} represents in this case the id of a server)
     */
    List<String> getServers();

    /**
     * Get the list of all servers currently running with a given prefix
     * Example: "lobby" prefix given -> return all servers with an id starting with lobby
     *
     * @return - A list of {@link String} (a {@link String} represents in this case the id of a server)
     */
    List<String> getServers(String prefix);

    /**
     * Get the actual best lobby server
     *
     * @return - A {@link String} which represents the id of the lobby server
     */
    String getLobbyServer();

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
     * @param server - A {@link String} which represents the id of a server
     */
    void sendPlayerToServer(UUID playerUuid, String server);


}
