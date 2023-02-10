package fr.hyriode.api.limbo;

import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 02/01/2023 at 17:24.<br>
 *
 * The manager to interact with limbo internal system.
 */
public interface IHyriLimboManager {

    /** The identifier of the limbos */
    String LIMBOS_ID = "limbo";

    /**
     * Get all the limbos started on the network
     *
     * @return A list of {@link HyggLimbo}
     */
    @NotNull Set<HyggLimbo> getLimbos();

    /**
     * Get all the limbos started on the network with a given type
     *
     * @param type The type of the limbos to get
     * @return A list of {@link HyggLimbo}
     */
    @NotNull Set<HyggLimbo> getLimbos(HyggLimbo.Type type);

    /**
     * Get a limbo object from its name
     *
     * @param name The name of the limbo to get
     * @return A {@link HyggLimbo} object
     */
    @Nullable HyggLimbo getLimbo(@NotNull String name);

    /**
     * Get the best limbo in terms of connected players
     *
     * @param type The type of the limbo to get
     * @return A {@link HyggLimbo} object
     */
    @Nullable HyggLimbo getBestLimbo(HyggLimbo.Type type);

    /**
     * Send a player to a given limbo
     *
     * @param playerId The {@link UUID} of the player to send
     * @param limboName The name of the limbo where the player will be sent
     */
    void sendPlayerToLimbo(@NotNull UUID playerId, @NotNull String limboName);

    /**
     * Create a new limbo
     *
     * @param type The type of the limbo to create
     * @param data The data of the limbo to create
     * @param onCreated The consumer triggered when the limbo is created
     */
    void createLimbo(@NotNull HyggLimbo.Type type, @NotNull HyggData data, @NotNull Consumer<HyggLimbo> onCreated);

    /**
     * Remove a limbo
     *
     * @param limboName The name of the limbo to remove
     * @param onRemoved The task triggered when the limbo is removed
     */
    void removeLimbo(@NotNull String limboName, Runnable onRemoved);

}
