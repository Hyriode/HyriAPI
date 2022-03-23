package fr.hyriode.api.event.model;

import fr.hyriode.api.event.HyriEvent;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 10:19
 */
public class HyriMotdChangedEvent extends HyriEvent {

    /** The old motd on the network */
    private final String oldMotd;
    /** The new motd on the network */
    private final String newMotd;

    /**
     * Constructor of {@link HyriMotdChangedEvent}
     *
     * @param oldMotd The old motd
     * @param newMotd The new motd
     */
    public HyriMotdChangedEvent(String oldMotd, String newMotd) {
        this.oldMotd = oldMotd;
        this.newMotd = newMotd;
    }

    /**
     * Get the old motd of the network
     *
     * @return A motd
     */
    public String getOldMotd() {
        return this.oldMotd;
    }

    /**
     * Get the new motd of the network
     *
     * @return A motd
     */
    public String getNewMotd() {
        return this.newMotd;
    }

}
