package fr.hyriode.api.guild;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Created by AstFaster
 * on 12/02/2023 at 23:52.<br>
 *
 * This class represents what a rank of a {@linkplain IHyriGuild guild} is.
 */
public interface IGuildRank {

    /** The regex that the names have to match */
    Pattern NAME_REGEX = Pattern.compile("^\\S(\\S|( ?)){2,14}\\S$");
    /** The regex that the tags have to match */
    Pattern TAG_REGEX = Pattern.compile("^[A-Z0-9]{1,6}$");

    /** The priority for the leader rank */
    int LEADER_PRIORITY = -1;

    /**
     * Get the name of the rank
     *
     * @return A rank name. E.g. Leader or Officer
     */
    @NotNull String getName();

    /**
     * Set the name of the rank
     *
     * @param name A new rank name
     */
    void setName(@NotNull String name);

    /**
     * Get the tag of the rank
     *
     * @return A tag
     */
    @Nullable String getTag();

    /**
     * Set the tag of the rank
     *
     * @param tag A new tag
     */
    void setTag(@Nullable String tag);

    /**
     * Check whether this rank is the default one of the guild
     *
     * @return <code>true</code> if yes
     */
    boolean isDefault();

    /**
     * Set whether this rank is the default one of the guild or not
     *
     * @param _default <code>true</code> if yes
     */
    void setDefault(boolean _default);

    /**
     * Check whether this rank is the leader one of the guild
     *
     * @return <code>true</code> if yes
     */
    boolean isLeader();

    /**
     * Get the priority of the rank
     *
     * @return A priority
     */
    int getPriority();

    /**
     * Increase the priority of the rank
     *
     * @return The new priority of the rank
     */
    int increasePriority();

    /**
     * Decrease the priority of the rank
     *
     * @return The new priority of the rank
     */
    int decreasePriority();

    /**
     * Get the timestamp when the rank was created
     *
     * @return A timestamp (in milliseconds)
     */
    long getCreatedDate();

}
