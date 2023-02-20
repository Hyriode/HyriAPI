package fr.hyriode.api.world;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 19/02/2023 at 12:37.<br>
 *
 * The manager of stored worlds (game maps, lobbies, etc).
 */
public interface IHyriWorldManager {

    /** The default category for worlds */
    String DEFAULT_CATEGORY = "DEFAULT";

    /**
     * Create a builder pattern for a {@linkplain IHyriWorld world}
     *
     * @return The created {@linkplain fr.hyriode.api.world.IHyriWorld.IBuilder builder}
     */
    @NotNull IHyriWorld.IBuilder newWorld();

    /**
     * Save a world in database
     *
     * @param world The information of the world
     * @param worldFolder The folder of the world
     */
    void saveWorld(@NotNull IHyriWorld world, @NotNull File worldFolder);

    /**
     * Save a world in database.<br>
     * Only works if HyriAPI is running in server mode!
     *
     * @param world The information of the world
     * @param worldId The Bukkit id of the world
     */
    void saveWorld(@NotNull IHyriWorld world, @NotNull UUID worldId);

    /**
     * Update the information of a world
     *
     * @param world The world to update
     */
    void updateWorld(@NotNull IHyriWorld world);

    /**
     * Load a world to a destination folder
     *
     * @param world The information of the world to load
     * @param destinationFolder The folder where the world will be loaded
     */
    void loadWorld(@NotNull IHyriWorld world, @NotNull File destinationFolder);

    /**
     * Delete a world
     *
     * @param world The world to delete
     */
    void deleteWorld(@NotNull IHyriWorld world);

    /**
     * Delete a world
     *
     * @param database The database of the world
     * @param category The category of the world
     * @param name The name of the world
     */
    void deleteWorld(@NotNull String database, @NotNull String category, @NotNull String name);

    /**
     * Delete a world
     *
     * @param database The database of the world
     * @param name The name of the world
     */
    void deleteWorld(@NotNull String database, @NotNull String name);

    /**
     * Get a world
     *
     * @param database The database of the world
     * @param category The category of the world
     * @param name The name of the world
     * @return The found {@link IHyriWorld} if it exists
     */
    IHyriWorld getWorld(@NotNull String database, @NotNull String category, @NotNull String name);

    /**
     * Get a world
     *
     * @param database The database of the world
     * @param name The name of the world
     * @return The found {@link IHyriWorld} if it exists
     */
    IHyriWorld getWorld(@NotNull String database, @NotNull String name);

    /**
     * Get all the worlds in a category stored in a database
     *
     * @param database The database of the worlds
     * @param category The category of the worlds
     * @return A list of {@link IHyriWorld}
     */
    @NotNull List<IHyriWorld> getWorlds(@NotNull String database, @NotNull String category);

    /**
     * Get all the worlds stored in a database (in all categories)
     *
     * @param database The database of the worlds
     * @return A list of {@link IHyriWorld}
     */
    @NotNull List<IHyriWorld> getWorlds(@NotNull String database);

}
