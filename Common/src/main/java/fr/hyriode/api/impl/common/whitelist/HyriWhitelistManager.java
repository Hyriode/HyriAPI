package fr.hyriode.api.impl.common.whitelist;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.whitelist.IHyriWhitelistManager;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/04/2022 at 07:08
 */
public class HyriWhitelistManager implements IHyriWhitelistManager {

    private static final String KEY = "whitelist:";

    @Override
    public void whitelistPlayer(String name, int seconds) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final String key = KEY + name.toLowerCase();

            jedis.set(key, "true");
            jedis.expire(key, seconds);
        });
    }

    @Override
    public void removePlayerFromWhitelist(String name) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.del(KEY + name.toLowerCase()));
    }

    @Override
    public boolean isWhitelisted(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(KEY + name.toLowerCase()));
    }

}
