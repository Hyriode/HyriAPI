package fr.hyriode.hyriapi.pubsub.receiver;

import fr.hyriode.hyriapi.pubsub.HyriPacket;

/**
 * Project: TestProject
 * Created by AstFaster
 * on 05/11/2021 at 10:27
 */
public interface IHyriPatternPacketReceiver {

    /**
     * Called when a packet is received on Redis PubSub
     *
     * @param pattern - Received packet pattern
     * @param channel - Received packet channel
     * @param packet - Received packet
     */
    void receive(String pattern, String channel, HyriPacket packet);

}
