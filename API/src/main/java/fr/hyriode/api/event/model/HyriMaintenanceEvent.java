package fr.hyriode.api.event.model;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriCancellableEvent;
import fr.hyriode.api.network.IHyriMaintenance;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 09:19
 */
public class HyriMaintenanceEvent extends HyriCancellableEvent {

    /** The action that happened */
    private final Action action;

    /**
     * Constructor of {@link HyriMaintenanceEvent}
     *
     * @param action The action
     */
    public HyriMaintenanceEvent(Action action) {
        this.action = action;
    }

    /**
     * Get the action that has been done
     *
     * @return An {@link Action}
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Get the maintenance instance
     *
     * @return The {@link IHyriMaintenance} instance
     */
    public IHyriMaintenance getMaintenance() {
        return HyriAPI.get().getNetwork().getMaintenance();
    }

    /**
     * The action that has been done on the maintenance
     */
    public enum Action {

        /** The maintenance has been enabled */
        ENABLED,
        /** The maintenance has been disabled */
        DISABLED,
        /** The reason of the maintenance has been changed */
        REASON_CHANGED

    }

}
