package fr.hyriode.api.impl.common.player.auth;

import fr.hyriode.api.player.auth.IHyriAuth;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 08/01/2023 at 14:11
 */
public class HyriAuth implements IHyriAuth {

    private final UUID player;
    private final String hash;

    public HyriAuth(UUID player, String hash) {
        this.player = player;
        this.hash = hash;
    }

    @Override
    public @NotNull UUID getPlayerId() {
        return this.player;
    }

    @Override
    public @NotNull String getHash() {
        return this.hash;
    }

    @Override
    public boolean authenticate(@NotNull String password) {
        return BCrypt.checkpw(password, this.hash);
    }

}
