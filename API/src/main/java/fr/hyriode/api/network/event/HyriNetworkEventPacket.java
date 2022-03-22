package fr.hyriode.api.network.event;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 14:19
 */
public class HyriNetworkEventPacket extends HyriPacket {

    /** The class of the event */
    private final String eventClass;
    /** The event object */
    private final String event;
    /** Is the event asynchronous */
    private final boolean asyncEvent;

    /**
     * Constructor of {@link HyriNetworkEventPacket}
     *  @param eventClass The class of the event
     * @param event The event object
     * @param asyncEvent Set if the event is asynchronous
     */
    public HyriNetworkEventPacket(Class<? extends HyriEvent> eventClass, HyriEvent event, boolean asyncEvent) {
        this.eventClass = eventClass.getName();
        this.event = HyriAPI.GSON.toJson(event);
        this.asyncEvent = asyncEvent;
    }

    /**
     * Get the class of the event that has been triggered
     *
     * @return A {@link Class}
     */
    public Class<?> getEventClass() {
        try {
            return Class.forName(this.eventClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the event that has been triggered
     *
     * @return A {@link HyriEvent}
     */
    public HyriEvent getEvent() {
        return (HyriEvent) HyriAPI.GSON.fromJson(this.event, this.getEventClass());
    }

    /**
     * Check if the event needs to be fired asynchronously
     *
     * @return <code>true</code> if yes
     */
    public boolean isAsyncEvent() {
        return this.asyncEvent;
    }

}
