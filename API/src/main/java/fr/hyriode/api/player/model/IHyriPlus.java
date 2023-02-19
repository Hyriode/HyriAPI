package fr.hyriode.api.player.model;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.PlayerRank;

import java.util.Set;

/**
 * Created by AstFaster
 * on 17/02/2023 at 17:01.<br>
 *
 * Represents the Hyri+ data of a {@linkplain IHyriPlayer player}
 */
public interface IHyriPlus {

    /** The priority of the Hyri+ in queues */
    int PRIORITY = PlayerRank.EPIC.getPriority() + 1;
    /** The priority of the Hyri+ in the tab list */
    int TAB_LIST_PRIORITY = PlayerRank.EPIC.getTabListPriority() + 1;

    /**
     * Enabled the Hyri+
     */
    void enable();

    /**
     * Check whether the Hyri+ is enabled for the player
     *
     * @return <code>true</code> if it is enabled
     */
    boolean has();

    /**
     * Check if the Hyri+ offer has expired
     *
     * @return <code>true</code> if the expiration date is passed
     */
    boolean hasExpire();

    /**
     * Get the color of the Hyri+
     *
     * @return The {@link HyriChatColor} of the Hyri+
     */
    HyriChatColor getColor();

    /**
     * Set the color of the Hyri+
     *
     * @param color The new color of the Hyri+
     */
    void setColor(HyriChatColor color);

    /**
     * Get the list of unlocked colors
     *
     * @return A list of {@link HyriChatColor}
     */
    Set<HyriChatColor> getColors();

    /**
     * Add an unlocked color
     *
     * @param color A {@link HyriChatColor} to add
     */
    void addColor(HyriChatColor color);

    /**
     * Get the duration of the Hyri+
     *
     * @return A time in milliseconds
     */
    long getDuration();

    /**
     * Set the duration of the Hyri+
     *
     * @param duration The new duration of the Hyri+
     */
    void setDuration(long duration);

    /**
     * Get the date when the Hyri+ has been enabled
     *
     * @return A timestamp (in milliseconds); or <code>null</code> if the Hyri+ isn't enabled.
     */
    long getEnabledDate();

}
