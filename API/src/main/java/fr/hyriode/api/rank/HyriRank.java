package fr.hyriode.api.rank;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.rank.type.IHyriRankType;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:00
 */
public class HyriRank {

    /** The separator of prefix and player display name */
    public static final String SEPARATOR = HyriChatColor.WHITE + "・";

    /** The prefix of the rank */
    private String prefix;
    /** The main color of the rank. This color will be used after the separator */
    private HyriChatColor mainColor;

    /** The player type of the rank */
    private HyriPlayerRankType playerType;
    /** The staff type of the rank */
    private HyriStaffRankType staffType;

    /**
     * Constructor of {@link HyriRank}
     *
     * @param prefix The prefix of the rank
     * @param mainColor The {@link HyriChatColor} of the rank
     * @param playerType The {@link HyriPlayerRankType}
     * @param staffType The {@link HyriStaffRankType}
     */
    public HyriRank(String prefix, HyriChatColor mainColor, HyriPlayerRankType playerType, HyriStaffRankType staffType) {
        this.prefix = prefix;
        this.mainColor = mainColor;
        this.playerType = playerType;
        this.staffType = staffType;
    }

    /**
     * Constructor of {@link HyriRank}.<br>
     * This constructor will create a rank from a player type
     *
     * @param playerType The {@link HyriPlayerRankType}
     */
    public HyriRank(HyriPlayerRankType playerType) {
        this(null, null, playerType, null);
    }

    /**
     * Check if the rank is a given type
     *
     * @param playerType The {@link HyriPlayerRankType}
     * @return <code>true</code> if the rank has the type
     */
    public boolean is(HyriPlayerRankType playerType) {
        return this.playerType.getId() == playerType.getId();
    }

    /**
     * Check if the rank is a given type
     *
     * @param staffType The {@link HyriStaffRankType}
     * @return <code>true</code> if the rank has the type
     */
    public boolean is(HyriStaffRankType staffType) {
        return this.staffType.getId() == staffType.getId();
    }

    /**
     * Check if the rank is necessary compared to another rank
     *
     * @param playerType The {@link HyriPlayerRankType} to check with
     * @return <code>true</code> if the rank is superior or equal
     */
    public boolean isSuperior(HyriPlayerRankType playerType) {
        return this.playerType.getId() >= playerType.getId();
    }

    /**
     * Check if the rank is necessary compared to another rank
     *
     * @param staffType The {@link HyriStaffRankType} to check with
     * @return <code>true</code> if the rank is superior or equal
     */
    public boolean isSuperior(HyriStaffRankType staffType) {
        return this.staffType.getId() >= staffType.getId();
    }

    /**
     * Get the prefix of the rank
     *
     * @return A prefix
     */
    public String getPrefix() {
        return this.prefix != null ? this.prefix : (this.staffType != null ? this.staffType.getDefaultPrefix() : this.playerType.getDefaultPrefix());
    }

    /**
     * Set the prefix of the rank
     *
     * @param prefix New prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Check if this rank is using a custom prefix or not
     *
     * @return <code>true</code> if yes
     */
    public boolean hasCustomPrefix() {
        return this.prefix != null;
    }

    /**
     * Get the main color of the rank
     *
     * @return A {@link HyriChatColor}
     */
    public HyriChatColor getMainColor() {
        return this.mainColor != null ? this.mainColor : (this.staffType != null ? this.staffType.getDefaultColor() : this.playerType.getDefaultColor());
    }

    /**
     * Set the main color of the rank
     *
     * @param mainColor A {@link HyriChatColor}
     */
    public void setMainColor(HyriChatColor mainColor) {
        this.mainColor = mainColor;
    }

    /**
     * Check if the rank is using the separator
     *
     * @return <code>true</code> if yes
     */
    public boolean withSeparator() {
        return this.getType().withSeparator();
    }

    /**
     * Get the main type of the rank
     *
     * @return A {@link IHyriRankType}
     */
    public IHyriRankType getType() {
        return this.isStaff() ? this.staffType : this.playerType;
    }

    /**
     * Get the player type of the rank
     *
     * @return A {@link HyriPlayerRankType}
     */
    public HyriPlayerRankType getPlayerType() {
        return this.playerType;
    }

    /**
     * Set the player type of the rank
     *
     * @param playerType New {@link HyriPlayerRankType}
     */
    public void setPlayerType(HyriPlayerRankType playerType) {
        this.playerType = playerType;
    }

    /**
     * Get the staff type of the rank
     *
     * @return A {@link HyriStaffRankType}
     */
    public HyriStaffRankType getStaffType() {
        return this.staffType;
    }

    /**
     * Set the staff type of the rank
     *
     * @param staffType New {@link HyriStaffRankType}
     */
    public void setStaffType(HyriStaffRankType staffType) {
        this.staffType = staffType;
    }

    /**
     * Check if the rank is a default rank
     *
     * @return <code>true</code> if yes
     */
    public boolean isDefault() {
        return this.playerType == HyriPlayerRankType.PLAYER && !this.isStaff();
    }

    /**
     * Check if the rank is staff
     *
     * @return <code>true</code> if yes
     */
    public boolean isStaff() {
        return this.staffType != null;
    }

    /**
     * Get the priority in queues of the rank
     *
     * @return A priority
     */
    public int getPriority() {
        return this.getType().getPriority();
    }

    /**
     * Get the priority in tab list of the rank
     *
     * @return A priority
     */
    public int getTabListPriority() {
        return this.getType().getTabListPriority();
    }

}
