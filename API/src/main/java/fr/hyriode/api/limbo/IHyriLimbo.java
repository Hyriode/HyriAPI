package fr.hyriode.api.limbo;

import fr.hyriode.api.application.IHyriApplication;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

import static fr.hyriode.hyggdrasil.api.limbo.HyggLimbo.State;
import static fr.hyriode.hyggdrasil.api.limbo.HyggLimbo.Type;

/**
 * Created by AstFaster
 * on 02/01/2023 at 17:16.<br>
 *
 * The object representing the limbo data.
 */
public interface IHyriLimbo extends IHyriApplication<State> {

    /**
     * Get the type of the limbo
     *
     * @return A {@link Type}
     */
    @NotNull Type getType();

    /**
     * Set the type of the limbo
     *
     * @param type A new {@link Type}
     */
    void setType(@NotNull Type type);

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
