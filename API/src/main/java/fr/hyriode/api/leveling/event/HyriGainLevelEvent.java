package fr.hyriode.api.leveling.event;

import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 15:55
 */
public class HyriGainLevelEvent extends HyriLevelingEvent {

    /** The old level of the player */
    private final int oldLevel;
    /** The new level of the player */
    private final int newLevel;

    /**
     * Constructor of {@link HyriGainLevelEvent}
     *
     * @param player The player that owns the leveling
     * @param leveling The leveling object
     * @param oldLevel The old level of the player
     * @param newLevel The new level of the player
     */
    public HyriGainLevelEvent(UUID player, String leveling, int oldLevel, int newLevel) {
        super(player, leveling);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    /**
     * Get the old level of the player
     *
     * @return A level
     */
    public int getOldLevel() {
        return this.oldLevel;
    }

    /**
     * Get the new level of the player
     *
     * @return A level
     */
    public int getNewLevel() {
        return this.newLevel;
    }

}
