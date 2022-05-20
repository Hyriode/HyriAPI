package fr.hyriode.api.packet.event;

import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: Hyriode
 * Created by AstFaster
 * on 20/05/2022 at 22:42
 */
public class HyriPacketSendEvent extends HyriPacketEvent {

    /**
     * Constructor of {@link HyriPacketSendEvent}
     *
     * @param packet  The packet
     * @param channel The channel
     */
    public HyriPacketSendEvent(HyriPacket packet, String channel) {
        super(packet, channel);
    }

}
