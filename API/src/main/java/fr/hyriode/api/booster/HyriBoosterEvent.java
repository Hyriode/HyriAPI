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
    /** The action done on the booster */
    private final Action action;

    /**
     * Constructor of {@link HyriBoosterEvent}
     *
     * @param booster The concerned booster
     * @param action An {@link Action}
     */
    public HyriBoosterEvent(IHyriBooster booster, Action action) {
        this.booster = booster;
        this.action = action;
    }

    /**
     * Get the booster that triggered the event
     *
     * @return A {@link IHyriBooster}
     */
    public IHyriBooster getBooster() {
        return this.booster;
    }

    /**
     * Get the action done on the booster
     *
     * @return An {@link Action}
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * The action that triggered the event
     */
    public enum Action {

        /** A booster has been enabled on the network */
        ENABLED,
        /** A booster has been removed on the network */
        REMOVED

    }

}
