package fr.hyriode.api.rank.hyriplus;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.transaction.IHyriTransactionContent;

import java.util.*;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 11:31
 */
public class HyriPlus {

    /** The priority of the Hyri+ in queues */
    public static final int PRIORITY = HyriPlayerRankType.EPIC.getPriority() + 1;
    /** The priority of the Hyri+ in the tab list */
    public static final int TAB_LIST_PRIORITY = HyriPlayerRankType.EPIC.getTabListPriority() + 1;

    /** The duration of the Hyri+ in milliseconds */
    private long duration;
    /** The timestamp when the Hyri+ has been enabled */
    private long enabledDate = -1;

    /** The color of the Hyri+ */
    private HyriChatColor color = HyriChatColor.LIGHT_PURPLE;
    /** The colors possessed by the player */
    private final List<HyriChatColor> colors = new ArrayList<>(Collections.singletonList(HyriChatColor.LIGHT_PURPLE));

    /**
     * Enabled the Hyri+
     */
    public void enable() {
        this.enabledDate = System.currentTimeMillis();
    }

    /**
     * Get the color of the Hyri+
     *
     * @return The {@link HyriChatColor} of the Hyri+
     */
    public HyriChatColor getColor() {
        return this.color;
    }

    /**
     * Set the color of the Hyri+
     *
     * @param color The new color of the Hyri+
     */
    public void setColor(HyriChatColor color) {
        this.color = color;
    }

    /**
     * Get the list of unlocked colors
     *
     * @return A list of {@link HyriChatColor}
     */
    public List<HyriChatColor> getColors() {
        return this.colors;
    }

    /**
     * Add an unlocked color
     *
     * @param color A {@link HyriChatColor} to add
     */
    public void addColor(HyriChatColor color) {
        this.colors.add(color);
    }

    /**
     * Get the duration of the Hyri+
     *
     * @return A time in milliseconds
     */
    public long getDuration() {
        return this.duration;
    }

    /**
     * Set the duration of the Hyri+
     *
     * @param duration The new duration of the Hyri+
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Get the date when the Hyri+ has been enabled
     *
     * @return A {@link Date} or <code>null</code> if the Hyri+ is not enabled
     */
    public Date getEnabledDate() {
        return this.enabledDate == -1 ? null : new Date(this.enabledDate);
    }

    /**
     * Check if the Hyri+ offer has expired
     *
     * @return <code>true</code> if the expiration date is passed
     */
    public boolean hasExpire() {
        final boolean result = System.currentTimeMillis() >= this.enabledDate + this.duration;

        if (result) {
            this.enabledDate = -1;
            this.duration = 0;
        }

        return result;
    }

}
