package fr.hyriode.api.impl.common.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.ILobbyAPI;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

import java.util.*;

/**
 * Created by AstFaster
 * on 22/11/2022 at 18:46
 */
public class LobbyAPI implements ILobbyAPI {

    @Override
    public HyggServer getBestLobby() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<String> lobbies = jedis.zrange(BALANCER_KEY, 0, 0);

            if (lobbies != null && lobbies.size() > 0) {
                return HyriAPI.get().getServerManager().getServer(lobbies.get(0));
            }
            return null;
        });
    }

    @Override
    public Set<HyggServer> getLobbies() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Set<HyggServer> result = new HashSet<>();
            final List<String> lobbies = jedis.zrange(BALANCER_KEY, 0, -1);

            if (lobbies != null) {
                for (String lobby : lobbies) {
                    result.add(HyriAPI.get().getServerManager().getServer(lobby));
                }
            }
            return Collections.unmodifiableSet(result);
        });
    }

    @Override
    public void sendPlayerToLobby(UUID playerId) {
        final HyggServer bestLobby = this.getBestLobby();

        if (bestLobby == null) {
            return;
        }

        HyriAPI.get().getServerManager().sendPlayerToServer(playerId, bestLobby.getName());
    }

}
