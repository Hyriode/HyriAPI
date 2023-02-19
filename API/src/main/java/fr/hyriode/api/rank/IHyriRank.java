package fr.hyriode.api.rank;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Created by AstFaster
 * on 17/02/2023 at 16:39.<br>
 *
 * Represents the rank of a {@linkplain IHyriPlayer player}.
 */
public interface IHyriRank {

    /** The separator of prefix and player display name */
    String SEPARATOR = HyriChatColor.WHITE + "・";

    /**
     * Check if the rank is a given type
     *
     * @param playerType The {@link PlayerRank}
     * @return <code>true</code> if the rank has the type
     */
    boolean is(PlayerRank playerType);

    /**
     * Check if the rank is a given type
     *
     * @param staffType The {@link StaffRank}
     * @return <code>true</code> if the rank has the type
     */
    boolean is(StaffRank staffType);

    /**
     * Check if the rank is necessary compared to another rank
     *
     * @param playerType The {@link PlayerRank} to check with
     * @return <code>true</code> if the rank is superior or equal
     */
    boolean isSuperior(PlayerRank playerType);

    /**
     * Check if the rank is necessary compared to another rank
     *
     * @param staffType The {@link StaffRank} to check with
     * @return <code>true</code> if the rank is superior or equal
     */
    boolean isSuperior(StaffRank staffType);

    /**
     * Get the prefix of the rank
     *
     * @return A prefix
     */
    String getPrefix();

    /**
     * Set the prefix of the rank
     *
     * @param prefix New prefix
     */
    void setPrefix(String prefix);

    /**
     * Check if this rank is using a custom prefix or not
     *
     * @return <code>true</code> if yes
     */
    boolean hasCustomPrefix();

    /**
     * Get the main color of the rank
     *
     * @return A {@link HyriChatColor}
     */
    HyriChatColor getMainColor();

    /**
     * Set the main color of the rank
     *
     * @param mainColor A {@link HyriChatColor}
     */
    void setMainColor(HyriChatColor mainColor);

    /**
     * Check if the rank is using the separator
     *
     * @return <code>true</code> if yes
     */
    boolean withSeparator();

    /**
     * Get the main type of the rank
     *
     * @return A {@link IHyriRankType}
     */
    IHyriRankType getType();

    /**
     * Get the player type of the rank
     *
     * @return A {@link PlayerRank}
     */
    PlayerRank getPlayerType();

    /**
     * Get the real player type without checking whether the player is staff or not
     *
     * @return A {@link PlayerRank}
     */
    PlayerRank getRealPlayerType();

    /**
     * Set the player type of the rank
     *
     * @param playerType New {@link PlayerRank}
     */
    void setPlayerType(PlayerRank playerType);

    /**
     * Get the staff type of the rank
     *
     * @return A {@link StaffRank}
     */
    StaffRank getStaffType();

    /**
     * Set the staff type of the rank
     *
     * @param staffType New {@link StaffRank}
     */
    void setStaffType(StaffRank staffType);

    /**
     * Check if the rank is a default rank
     *
     * @return <code>true</code> if yes
     */
    boolean isDefault();

    /**
     * Check if the rank is staff
     *
     * @return <code>true</code> if yes
     */
    boolean isStaff();

    /**
     * Get the priority in queues of the rank
     *
     * @return A priority
     */
    int getPriority();

    /**
     * Get the priority in tab list of the rank
     *
     * @return A priority
     */
    int getTabListPriority();

}
