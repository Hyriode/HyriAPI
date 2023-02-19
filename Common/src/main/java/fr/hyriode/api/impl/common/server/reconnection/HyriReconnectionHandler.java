package fr.hyriode.api.impl.common.server.reconnection;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.reconnection.IHyriReconnectionData;
import fr.hyriode.api.server.reconnection.IHyriReconnectionHandler;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import redis.clients.jedis.Pipeline;

import java.util.UUID;
import java.util.function.Function;

/**
 * Created by AstFaster
 * on 25/05/2022 at 07:13
 */
public class HyriReconnectionHandler implements IHyriReconnectionHandler {

    private static final Function<UUID, String> KEY = uuid -> "reconnection:" + uuid.toString();

    @Override
    public void reconnectPlayer(IHyriReconnectionData reconnectionData) {
        final HyggServer server = HyriAPI.get().getServerManager().getServer(reconnectionData.getServerName());
        final UUID playerId = reconnectionData.getPlayerId();

        if (server == null) {
            return;
        }

        this.remove(playerId);

        HyriAPI.get().getServerManager().sendPlayerToServer(playerId, server.getName());
    }

    @Override
    public IHyriReconnectionData get(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String key = KEY.apply(playerId);
            final String serverName = jedis.get(key);

            if (serverName == null) {
                return null;
            }
            return new HyriReconnectionData(playerId, serverName, jedis.ttl(key));
        });
    }

    @Override
    public void set(UUID playerId, String serverName, long ttl) {
        final String key = KEY.apply(playerId);

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final Pipeline pipeline = jedis.pipelined();

            pipeline.set(key, serverName);
            pipeline.expire(key, ttl);
            pipeline.sync();
        });
    }

    @Override
    public void remove(UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY.apply(playerId)));
    }

}
