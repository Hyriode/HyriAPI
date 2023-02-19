package fr.hyriode.api.guild;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 11/02/2023 at 22:36.<br>
 *
 * This class represents the manager of {@linkplain IHyriGuild guilds}.
 */
public interface IHyriGuildManager {

    /**
     * Create a new guild
     *
     * @param playerId The {@link UUID} of the player
     * @param name The name of the guild to create
     * @return The created {@link IHyriGuild}
     */
    @NotNull IHyriGuild createGuild(@NotNull UUID playerId, @NotNull String name);

    /**
     * Get a guild by its id
     *
     * @param guildId The id of the guild
     * @return The found {@link IHyriGuild}
     */
    @Nullable IHyriGuild getGuild(@NotNull ObjectId guildId);

    /**
     * Get a guild by its name
     *
     * @param name The name of the guild
     * @return The found {@link IHyriGuild}
     */
    @Nullable IHyriGuild getGuild(@NotNull String name);

    /**
     * Save a given guild
     *
     * @param guild The guild to save
     */
    void saveGuild(@NotNull IHyriGuild guild);

    /**
     * Remove a guild
     *
     * @param guildId The id of the guild to remove
     */
    void removeGuild(@NotNull ObjectId guildId);

    /**
     * Get the id of a guild from its name
     *
     * @param name The name of the guild
     * @return The found {@link ObjectId}
     */
    @Nullable ObjectId getGuildIdFromName(@NotNull String name);

    /**
     * Get the id of a guild from its tag
     *
     * @param tag The tag of the guild
     * @return The found {@link ObjectId}
     */
    @Nullable ObjectId getGuildIdFromTag(@NotNull String tag);

    /**
     * Check whether a given name exists or not
     *
     * @param name The name to check
     * @return <code>true</code> if it exists
     */
    boolean isNameExisting(@NotNull String name);

    /**
     * Check whether a given tag exists or not
     *
     * @param tag The tag to check
     * @return <code>true</code> if it exists
     */
    boolean isTagExisting(@NotNull String tag);

}
