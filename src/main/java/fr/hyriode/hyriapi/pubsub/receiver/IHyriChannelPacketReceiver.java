package fr.hyriode.hyriapi.pubsub.receiver;

import fr.hyriode.hyriapi.pubsub.HyriPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/11/2021 at 10:27
 */
public interface IHyriChannelPacketReceiver {

    /**
     * Called when a packet is received on Redis PubSub
     *
     * @param channel - Received packet channel
     * @param packet - Received packet
     */
    void receive(String channel, HyriPacket packet);

}
