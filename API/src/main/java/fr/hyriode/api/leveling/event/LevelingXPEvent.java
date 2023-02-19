package fr.hyriode.api.leveling.event;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/05/2022 at 16:39
 */
public class LevelingXPEvent extends LevelingEvent {

    private final double oldExperience;
    private final double newExperience;

    public LevelingXPEvent(String leveling, double oldExperience, double newExperience) {
        super(leveling);
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
