package fr.hyriode.api.impl.common.player.nickname;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.nickname.IHyriNicknameManager;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 22:00
 */
public class HyriNicknameManager implements IHyriNicknameManager {

    private static final String REDIS_KEY = "nicknames:";

    @Override
    public boolean isNicknameAvailable(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(REDIS_KEY + name.toLowerCase()) == null) && HyriAPI.get().getPlayerManager().getPlayerId(name) == null;
    }

    @Override
    public void addUsedNickname(String name, UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(REDIS_KEY + name.toLowerCase(), playerId.toString()));
    }

    @Override
    public void removeUsedNickname(String name) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(REDIS_KEY + name.toLowerCase()));
    }

    @Override
    public UUID getPlayerUsingNickname(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String uuidStr = jedis.get(REDIS_KEY + name.toLowerCase());

            if (uuidStr != null) {
                return UUID.fromString(uuidStr);
            }
            return null;
        });
    }

}
