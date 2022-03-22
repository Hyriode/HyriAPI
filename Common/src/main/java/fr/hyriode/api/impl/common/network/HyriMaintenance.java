package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriCancellableEvent;
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

    public void update() {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(HyriNetwork.MAINTENANCE_KEY, HyriAPI.GSON.toJson(this)));
    }

    @Override
    public boolean enable(UUID trigger, String reason) {
        if (!this.active) {
            if (this.triggerEvent(HyriMaintenanceEvent.Action.ENABLED)) {
                this.active = true;
                this.trigger = trigger;
                this.reason = reason;

                this.update();

                return true;
            }

            this.disable();
        }
        return false;
    }

    @Override
    public boolean disable() {
        if (this.active) {
            if (this.triggerEvent(HyriMaintenanceEvent.Action.DISABLED)) {
                this.active = false;
                this.trigger = null;
                this.reason = null;

                this.update();

                return true;
            }

            this.enable(this.trigger, this.reason);
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
            if (this.triggerEvent(HyriMaintenanceEvent.Action.REASON_CHANGED)) {
                this.reason = reason;

                this.update();
            }
        }
    }

    private boolean triggerEvent(HyriMaintenanceEvent.Action action) {
        final HyriCancellableEvent event = new HyriMaintenanceEvent(action);

        HyriAPI.get().getNetwork().getEventBus().publish(event);

        return !event.isCancelled();
    }

}
