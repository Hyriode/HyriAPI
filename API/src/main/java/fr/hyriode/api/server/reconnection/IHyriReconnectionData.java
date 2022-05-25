package fr.hyriode.api.server.reconnection;

import fr.hyriode.api.HyriAPI;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/05/2022 at 12:57
 */
public interface IHyriReconnectionData {

    UUID getPlayerId();

    String getServerName();

    long getTTL();

    default void reconnect() {
        HyriAPI.get().getServerManager().getReconnectionHandler().reconnectPlayer(this);
    }

}
