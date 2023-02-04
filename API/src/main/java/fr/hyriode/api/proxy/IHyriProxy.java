package fr.hyriode.api.proxy;

import fr.hyriode.api.application.IHyriApplication;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public interface IHyriProxy extends IHyriApplication<HyggProxy.State> {

    /**
     * Get the players connected through the proxy
     *
     * @return Server players
     */
    @NotNull
    Set<UUID> getPlayers();

    /**
     * Add a player
     *
     * @param player The player to add
     */
    void addPlayer(@NotNull UUID player);

    /**
     * Remove a player
     *
     * @param player The player to remove
     */
    void removePlayer(@NotNull UUID player);

    /**
     * Get the proxy data
     *
     * @return A {@link HyggData}
     */
    @NotNull
    HyggData getData();

}
