package fr.hyriode.api.guild;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by AstFaster
 * on 11/02/2023 at 08:59.<br>
 *
 * This class represents what a guild is.<br>
 * A guild is basically a structure which regroups players.
 */
public interface IHyriGuild {

    /** The regex that the names have to match */
    Pattern NAME_REGEX = Pattern.compile("^\\S(\\S|( ?)){1,32}\\S$");
    /** The regex that the tags have to match */
    Pattern TAG_REGEX = Pattern.compile("^[A-Z0-9]{1,6}$");

    /** The maximum amount of lines that can have the MOTD */
    int MAX_MOTD_LINES = 8;
    /** The maximum amount of ranks that can have the guild */
    int MAX_RANKS = 7;

    /**
     * Get the id of the guild
     *
     * @return A {@link ObjectId}
     */
    ObjectId getId();

    /**
     * Get the name of the guild
     *
     * @return A guild name
     */
    @NotNull String getName();

    /**
     * Set the name of the guild.<br>
     * Name must match {@link IHyriGuild#NAME_REGEX}
     *
     * @param name A new guild name
     */
    void setName(@NotNull String name);

    /**
     * Get the tag of the guild.<br>
     *
     * @return A tag
     */
    @Nullable String getTag();

    /**
     * Set the tag of the guild.<br>
     * Tag must match {@link IHyriGuild#TAG_REGEX}
     *
     * @param tag A new tag
     */
    void setTag(@Nullable String tag);

    /**
     * Get the color of the tag
     *
     * @return A {@link HyriChatColor}
     */
    @NotNull HyriChatColor getTagColor();

    /**
     * Set the color of the tag
     *
     * @param tagColor A new {@link HyriChatColor}
     */
    void setTagColor(@NotNull HyriChatColor tagColor);

    /**
     * Get the description of the guild
     *
     * @return A description
     */
    @Nullable String getDescription();

    /**
     * Set the description of the guild
     *
     * @param description A new description
     */
    void setDescription(@Nullable String description);

    /**
     * Get the MOTD (Message Of The Day) of the guild
     *
     * @return A MOTD (a list of line)
     */
    @NotNull List<String> getMOTD();

    /**
     * Set the MOTD (Message Of The Day) of the guild
     *
     * @param motd A new MOTD
     */
    void setMOTD(@NotNull List<String> motd);

    /**
     * Set a line of the guild's motd
     *
     * @param line The index of the line
     * @param value The value of the line
     */
    void setMOTDLine(int line, @NotNull String value);

    /**
     * Add a line to the MOTD
     *
     * @param value The value of the line
     */
    void addMOTDLine(@NotNull String value);

    /**
     * Clear the MOTD
     */
    void clearMOTD();

    /**
     * Get the banner of the guild
     *
     * @return A banner
     */
    @Nullable String getBanner();

    /**
     * Set the banner of the guild
     *
     * @param banner A new banner
     */
    void setBanner(@Nullable String banner);

    /**
     * Get the slots of the guild
     *
     * @return The slots of the guild
     */
    int getSlots();

    /**
     * Add slots to the guild
     *
     * @param slots The slots to add
     */
    void addSlots(int slots);

    /**
     * Get the leader of the guild
     *
     * @return A {@link IGuildMember} object
     */
    @NotNull IGuildMember getLeader();

    /**
     * Set the new leader of the guild
     *
     * @param leaderId The new leader {@link UUID}
     */
    void setLeader(@NotNull UUID leaderId);

    /**
     * Get all the members of the guild
     *
     * @return A list of {@link IGuildMember}
     */
    @NotNull List<IGuildMember> getMembers();

    /**
     * Get a member of the guild
     *
     * @param memberId The {@link UUID} of the member to get
     * @return The found {@link IGuildMember}
     */
    @Nullable IGuildMember getMember(@NotNull UUID memberId);

    /**
     * Add a new member to the guild
     *
     * @param memberId The {@link UUID} of the member to add
     * @return The created {@link IGuildMember}
     */
    @NotNull IGuildMember addMember(@NotNull UUID memberId);

    /**
     * Remove a member of the guild
     *
     * @param memberId The {@link UUID} of the member to remove
     */
    void removeMember(@NotNull UUID memberId);

    /**
     * Check whether the guild contains a given player or not
     *
     * @param memberId The {@link UUID} of the player
     * @return <code>true</code> if he is in the guild
     */
    boolean hasMember(@NotNull UUID memberId);

    /**
     * Get all the ranks of the guild
     *
     * @return A list of {@link IGuildRank}
     */
    @NotNull List<IGuildRank> getRanks();

    /**
     * Get all the ranks of the guild but stored by their priorities ({@link IGuildRank#getPriority()}
     *
     * @return A list of {@link IGuildRank}
     */
    @NotNull List<IGuildRank> getRanksSorted();

    /**
     * Get a rank by its name
     *
     * @param rankName The name of the rank
     * @return The found {@link IGuildRank}
     */
    @Nullable IGuildRank getRank(@NotNull String rankName);

    /**
     * Get the default rank of the guild
     *
     * @return A {@link IGuildRank}
     */
    @NotNull IGuildRank getDefaultRank();

    /**
     * Get the leader rank of the guild
     *
     * @return A {@link IGuildRank}
     */
    @NotNull IGuildRank getLeaderRank();

    /**
     * Create a new rank for the guild
     *
     * @param rankName The name of the rank to create
     * @return The created {@link IGuildRank}
     */
    @NotNull IGuildRank createRank(@NotNull String rankName);

    /**
     * Remove a rank from the guild
     *
     * @param rankName The name of the rank to remove
     */
    void removeRank(@NotNull String rankName);

    /**
     * Get the leveling of the guild
     *
     * @return The {@link GuildLeveling} instance
     */
    @NotNull GuildLeveling getLeveling();

    /**
     * Get the chest of the guild
     *
     * @return The {@link IGuildChest} instance
     */
    @NotNull IGuildChest getChest();

    /**
     * Update the guild by triggering {@link IHyriGuildManager#saveGuild(IHyriGuild)}
     */
    default void update() {
        HyriAPI.get().getGuildManager().saveGuild(this);
    }

    /**
     * Get a guild by its id
     *
     * @param id The id of the guild
     * @return The found {@link IHyriGuild}
     */
    static IHyriGuild get(ObjectId id) {
        return HyriAPI.get().getGuildManager().getGuild(id);
    }

}
