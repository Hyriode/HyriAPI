package fr.hyriode.api.impl.common.server.reconnection;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.server.join.packet.HyriPlayerReconnectPacket;
import fr.hyriode.api.server.reconnection.IHyriReconnectionData;
import fr.hyriode.api.server.reconnection.IHyriReconnectionHandler;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

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

        if (server == null) {
            return;
        }

        HyriAPI.get().getPubSub().send(HyriChannel.JOIN, new HyriPlayerReconnectPacket(server.getName(), reconnectionData.getPlayerId()));
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
    public void set(IHyriReconnectionData data) {
        final String key = KEY.apply(data.getPlayerId());

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            jedis.set(key, data.getServerName());
            jedis.expire(key, data.getTTL());
        });
    }

    @Override
    public void remove(UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY.apply(playerId)));
    }

}
