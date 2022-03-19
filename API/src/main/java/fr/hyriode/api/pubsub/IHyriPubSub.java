package fr.hyriode.api.pubsub;

import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/11/2021 at 11:38
 */
public interface IHyriPubSub {

    /**
     * Subscribe a {@link IHyriPacketReceiver} on a given channel
     *
     * @param channel - Channel to listen
     * @param receiver - {@link IHyriPacketReceiver} to subscribe
     */
    void subscribe(String channel, IHyriPacketReceiver receiver);

    /**
     * Send a packet on a given channel
     *
     * @param channel - Channel to send packet
     * @param packet - Packet to send
     * @param callback - Callback to fire after sending packet
     */
    void send(String channel, HyriPacket packet, Runnable callback);

    /**
     * Send a packet on a given channel
     *
     * @param channel - Channel to send packet
     * @param packet - Packet to send
     */
    void send(String channel, HyriPacket packet);

}
