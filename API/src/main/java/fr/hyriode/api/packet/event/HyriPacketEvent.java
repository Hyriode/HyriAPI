package fr.hyriode.api.packet.event;

import fr.hyriode.api.event.HyriCancellableEvent;
import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: Hyriode
 * Created by AstFaster
 * on 20/05/2022 at 22:40
 */
public abstract class HyriPacketEvent extends HyriCancellableEvent {

    /** The packet that fired the event */
    protected HyriPacket packet;
    /** The channel related to the packet */
    protected String channel;

    /**
     * Constructor of {@link HyriPacketEvent}
     *
     * @param packet The packet
     * @param channel The channel
     */
    public HyriPacketEvent(HyriPacket packet, String channel) {
        this.packet = packet;
        this.channel = channel;
    }

    /**
     * Get the packet that triggered the event
     *
     * @return The {@linkplain HyriPacket packet} object
     */
    public HyriPacket getPacket() {
        return this.packet;
    }

    /**
     * Change the packet
     *
     * @param packet The new packet
     */
    public void changePacket(HyriPacket packet) {
        this.packet = packet;
    }

    /**
     * Get the channel related to the packet
     *
     * @return A channel
     */
    public String getChannel() {
        return this.channel;
    }

}
