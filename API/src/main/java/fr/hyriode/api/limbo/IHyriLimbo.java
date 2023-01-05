package fr.hyriode.api.limbo;

import fr.hyriode.api.application.IHyriApplication;
import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 02/01/2023 at 17:16.<br>
 *
 * The object representing the limbo data.
 */
public interface IHyriLimbo extends IHyriApplication<HyggLimbo.State> {

    /**
     * Get the data of the limbo
     *
     * @return The {@link HyggData} object linked to the limbo
     */
    @NotNull HyggData getData();

    /**
     * Get the players connected on the limbo
     *
     * @return A list of player {@link UUID}
     */
    @NotNull Set<UUID> getPlayers();

    /**
     * Add a player connected on the limbo
     *
     * @param player A player {@link UUID}
     */
    void addPlayer(@NotNull UUID player);

    /**
     * Remove a player connected on the limbo
     *
     * @param player A player {@link UUID}
     */
    void removePlayer(@NotNull UUID player);

}
