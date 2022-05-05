package fr.hyriode.api.leveling.event;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/05/2022 at 16:39
 */
public class HyriGainXPEvent extends HyriLevelingEvent {

    private final double oldExperience;
    private final double newExperience;

    public HyriGainXPEvent(UUID player, String leveling, double oldExperience, double newExperience) {
        super(player, leveling);
        this.oldExperience = oldExperience;
        this.newExperience = newExperience;
    }

    public double getOldExperience() {
        return this.oldExperience;
    }

    public double getNewExperience() {
        return this.newExperience;
    }

}
