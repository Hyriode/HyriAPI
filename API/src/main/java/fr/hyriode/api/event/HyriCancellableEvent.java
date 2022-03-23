package fr.hyriode.api.event;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 10/03/2022 at 18:18
 */
public abstract class HyriCancellableEvent extends HyriEvent {

    /** The boolean that represents if the event has been cancelled or not */
    protected boolean cancelled;

    /**
     * Check if the event is cancelled
     *
     * @return <code>true</code> if yes
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set if the event needs to be cancelled
     *
     * @param cancelled New boolean value
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
