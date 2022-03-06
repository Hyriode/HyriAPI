package fr.hyriode.api.packet;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 16:55
 */
public interface IHyriPacketReceiver {

    /**
     * Fired when a packet is received on the channel
     *
     * @param channel The channel where the packet was received
     * @param packet The packet received
     */
    void receive(String channel, HyriPacket packet);

}
