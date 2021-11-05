package fr.hyriode.hyriapi.pubsub;

import fr.hyriode.hyriapi.pubsub.receiver.IHyriChannelPacketReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriChannelReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriPatternPacketReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriPatternReceiver;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/11/2021 at 11:38
 */
public interface IHyriPubSub {

    /**
     * Subscribe a {@link IHyriChannelReceiver} on a given channel
     *
     * @param channel - Channel to listen
     * @param receiver - {@link IHyriChannelReceiver} to subscribe
     */
    void subscribe(String channel, IHyriChannelReceiver receiver);

    /**
     * Subscribe a {@link IHyriChannelPacketReceiver} on a given channel
     *
     * @param channel - Channel to listen
     * @param receiver - {@link IHyriChannelPacketReceiver} to subscribe
     */
    void subscribe(String channel, IHyriChannelPacketReceiver receiver);

    /**
     * Subscribe a {@link IHyriPatternReceiver} on a given pattern
     *
     * @param pattern - Channel to listen
     * @param receiver - {@link IHyriPatternReceiver} to subscribe
     */
    void subscribe(String pattern, IHyriPatternReceiver receiver);

    /**
     * Subscribe a {@link IHyriPatternPacketReceiver} on a given pattern
     *
     * @param pattern - Channel to listen
     * @param receiver - {@link IHyriPatternPacketReceiver} to subscribe
     */
    void subscribe(String pattern, IHyriPatternPacketReceiver receiver);

    /**
     * Send a message on a given channel
     *
     * @param channel - Channel to send message
     * @param message - Message to send
     * @param callback - Callback to fire after sending message
     */
    void send(String channel, String message, Runnable callback);

    /**
     * Send a message on a given channel
     *
     * @param channel - Channel to send message
     * @param message - Message to send
     */
    void send(String channel, String message);

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
