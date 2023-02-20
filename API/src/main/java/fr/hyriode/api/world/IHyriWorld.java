package fr.hyriode.api.world;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 19/02/2023 at 12:41.<br>
 *
 * Represents what a world is.
 */
public interface IHyriWorld {

    /**
     * Get the name of the database where the world is stored. (e.g. a game name such as bedwars)
     *
     * @return A database name
     */
    @NotNull String getDatabase();

    /**
     * Get the category of the world. (e.g. a game type such as SOLO or DOUBLES)
     *
     * @return A category
     */
    @NotNull String getCategory();

    /**
     * Set the category of the world
     *
     * @param category The new category
     */
    void setCategory(@NotNull String category);

    /**
     * Get the name of the world.<br>
     * This name is the one displayed to players.
     *
     * @return A name
     */
    @NotNull String getName();

    /**
     * Set the name of the world
     *
     * @param name The new name of the world
     */
    void setName(@NotNull String name);

    /**
     * Check whether the world is enabled
     *
     * @return <code>true</code> if the world is enabled
     */
    boolean isEnabled();

    /**
     * Set whether the world is enabled
     *
     * @param enabled <code>true</code> to enable
     */
    void setEnabled(boolean enabled);

    /**
     * Get the timestamp when the world was created/published
     *
     * @return A timestamp (in milliseconds)
     */
    long getCreationDate();

    /**
     * Get the authors of the world
     *
     * @return A list of username
     */
    @NotNull List<String> getAuthors();

    /**
     * Add an author of the world
     *
     * @param author A player username
     */
    void addAuthor(@NotNull String author);

    /**
     * Remove an author of the world
     *
     * @param author A player username
     */
    void removeAuthor(@NotNull String author);

    /**
     * Save the world from a folder
     *
     * @param worldFolder The folder where the world is stored
     */
    void save(@NotNull File worldFolder);

    /**
     * Save the world from its Bukkit id.<br>
     * Only works if HyriAPI is running in server mode!
     *
     * @param worldId The id of the world
     */
    void save(@NotNull UUID worldId);

    /**
     * Load the world in a folder
     *
     * @param destinationFolder The folder where the world will be loaded
     */
    void load(@NotNull File destinationFolder);

    /**
     * Delete the world
     */
    void delete();

    /**
     * Update the world information
     */
    void update();

    /**
     * The builder class of a {@link IHyriWorld}
     */
    interface IBuilder {

        /**
         * Set the database where the world will be stored
         *
         * @param database A database name (e.g. bedwars)
         * @return This {@link IBuilder} instance
         */
        @NotNull IBuilder withDatabase(@NotNull String database);

        /**
         * Set the category of the world
         *
         * @param category A category (e.g. SOLO or DOUBLES)
         * @return This {@link IBuilder} instance
         */
        @NotNull IBuilder withCategory(@NotNull String category);

        /**
         * Set the name of the world
         *
         * @param name A world name
         * @return This {@link IBuilder} instance
         */
        @NotNull IBuilder withName(@NotNull String name);

        /**
         * Set the authors of the world
         *
         * @param authors A list of player username
         * @return This {@link IBuilder} instance
         */
        @NotNull IBuilder withAuthors(@NotNull List<String> authors);

        /**
         * Add authors of the world
         *
         * @param authors An array of player username
         * @return This {@link IBuilder} instance
         */
        @NotNull IBuilder addAuthors(@NotNull String... authors);

        /**
         * Create the world object
         *
         * @return The created {@linkplain IHyriWorld world} object
         */
        @NotNull IHyriWorld build();

    }

}
