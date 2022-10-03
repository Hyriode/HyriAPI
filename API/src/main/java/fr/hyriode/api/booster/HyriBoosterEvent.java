package fr.hyriode.api.booster;

import fr.hyriode.api.event.HyriEvent;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 15:29
 */
public class HyriBoosterEvent extends HyriEvent {

    /** The booster that triggered the event */
    private final IHyriBooster booster;

    /**
     * Constructor of {@link HyriBoosterEvent}
     *
     * @param booster The concerned booster
     */
    public HyriBoosterEvent(IHyriBooster booster) {
        this.booster = booster;
    }

    /**
     * Get the booster that triggered the event
     *
     * @return A {@link IHyriBooster}
     */
    public IHyriBooster getBooster() {
        return this.booster;
    }

}
