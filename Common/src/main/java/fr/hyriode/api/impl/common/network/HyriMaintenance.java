package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.model.HyriMaintenanceEvent;
import fr.hyriode.api.network.IHyriMaintenance;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 09:08
 */
public class HyriMaintenance implements IHyriMaintenance {

    private boolean active;
    private UUID trigger;
    private String reason;

    @Override
    public boolean enable(UUID trigger, String reason) {
        if (!this.active) {
            this.active = true;
            this.trigger = trigger;
            this.reason = reason;

            this.triggerEvent(HyriMaintenanceEvent.Action.ENABLED);

            return true;
        }
        return false;
    }

    @Override
    public boolean disable() {
        if (this.active) {
            this.active = false;
            this.trigger = null;
            this.reason = null;

            this.triggerEvent(HyriMaintenanceEvent.Action.DISABLED);

            return true;
        }
        return false;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public UUID getTrigger() {
        return this.trigger;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        if (this.active) {
            this.reason = reason;

            this.triggerEvent(HyriMaintenanceEvent.Action.REASON_CHANGED);
        }
    }

    private void triggerEvent(HyriMaintenanceEvent.Action action) {
        HyriAPI.get().getNetworkManager().getEventBus().publish(new HyriMaintenanceEvent(action));
    }

}
