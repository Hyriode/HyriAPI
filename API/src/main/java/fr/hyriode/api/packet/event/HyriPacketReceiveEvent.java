package fr.hyriode.api.packet.event;

import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: Hyriode
 * Created by AstFaster
 * on 20/05/2022 at 22:43
 */
public class HyriPacketReceiveEvent extends HyriPacketEvent {

    /**
     * Constructor of {@link HyriPacketReceiveEvent}
     *
     * @param packet  The packet
     * @param channel The channel
     */
    public HyriPacketReceiveEvent(HyriPacket packet, String channel) {
        super(packet, channel);
    }

}
