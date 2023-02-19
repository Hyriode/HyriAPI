package fr.hyriode.api.leveling.event;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 15:55
 */
public class LevelingLevelEvent extends LevelingEvent {

    /** The old level of the player */
    private final int oldLevel;
    /** The new level of the player */
    private final int newLevel;

    /**
     * Constructor of {@link LevelingLevelEvent}
     *
     * @param leveling The name of the leveling
     * @param oldLevel The old level of the player
     * @param newLevel The new level of the player
     */
    public LevelingLevelEvent(String leveling, int oldLevel, int newLevel) {
        super(leveling);
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
