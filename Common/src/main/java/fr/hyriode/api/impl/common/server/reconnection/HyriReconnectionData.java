package fr.hyriode.api.impl.common.server.reconnection;

import fr.hyriode.api.server.reconnection.IHyriReconnectionData;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/05/2022 at 12:58
 */
public class HyriReconnectionData implements IHyriReconnectionData {

    private final UUID playerId;
    private final String serverName;
    private final long ttl;

    public HyriReconnectionData(UUID playerId, String serverName, long ttl) {
        this.playerId = playerId;
        this.serverName = serverName;
        this.ttl = ttl;
    }

    @Override
    public UUID getPlayerId() {
        return this.playerId;
    }

    @Override
    public String getServerName() {
        return this.serverName;
    }

    @Override
    public long getTTL() {
        return this.ttl;
    }

}
