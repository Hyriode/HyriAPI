package fr.hyriode.api.impl.common.player.auth;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.auth.IHyriAuth;
import fr.hyriode.api.player.auth.IHyriAuthManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 08/01/2023 at 14:11
 */
public class HyriAuthManager implements IHyriAuthManager {

    private static final String REDIS_KEY = "auth:";

    @Override
    public @NotNull IHyriAuth newAuth(@NotNull UUID playerId, @NotNull String password) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final IHyriAuth auth = new HyriAuth(playerId, BCrypt.hashpw(password, BCrypt.gensalt(6)));

            jedis.set(REDIS_KEY + playerId, auth.getHash());

            return auth;
        });
    }

    @Override
    public void deleteAuth(@NotNull UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(REDIS_KEY + playerId));
    }

    @Override
    public @Nullable IHyriAuth getAuth(@NotNull UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String hash = jedis.get(REDIS_KEY + playerId);

            return hash != null ? new HyriAuth(playerId, hash) : null;
        });
    }

    @Override
    public boolean hasAuth(@NotNull UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(REDIS_KEY + playerId));
    }

}
