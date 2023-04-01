package fr.hyriode.api.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 15:29
 */
public class HyriBoosterEvent extends HyriEvent {

    /** The booster that triggered the event */
    private final UUID boosterId;

    /**
     * Constructor of {@link HyriBoosterEvent}
     *
     * @param boosterId The if of the concerned booster
     */
    public HyriBoosterEvent(UUID boosterId) {
        this.boosterId = boosterId;
    }

    /**
     * Get the booster that triggered the event
     *
     * @return A {@link IHyriBooster}
     */
    public IHyriBooster getBooster() {
        return HyriAPI.get().getBoosterManager().getBooster(this.boosterId);
    }

}
