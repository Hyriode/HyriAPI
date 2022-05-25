package fr.hyriode.api.server.reconnection;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/05/2022 at 07:09
 */
public interface IHyriReconnectionHandler {

    void reconnectPlayer(IHyriReconnectionData reconnectionData);

    IHyriReconnectionData get(UUID playerId);

    void set(UUID playerId, String serverName, long ttl);

    void remove(UUID playerId);

}
