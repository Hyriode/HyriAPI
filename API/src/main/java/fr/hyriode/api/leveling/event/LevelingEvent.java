package fr.hyriode.api.leveling.event;

import fr.hyriode.api.event.HyriEvent;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 15:48
 */
public abstract class LevelingEvent extends HyriEvent {

    /** The leveling object */
    private final String leveling;

    /**
     * Constructor of {@link LevelingEvent}
     *
     * @param leveling The leveling
     */
    public LevelingEvent(String leveling) {
        this.leveling = leveling;
    }

    public String getLeveling() {
        return this.leveling;
    }

}
