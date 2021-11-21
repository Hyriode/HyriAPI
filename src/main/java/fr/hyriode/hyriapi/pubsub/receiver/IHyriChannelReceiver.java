package fr.hyriode.hyriapi.pubsub.receiver;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 04/11/2021 at 10:34
 */
public interface IHyriChannelReceiver {

    /**
     * Called when a message is received on Redis PubSub
     *
     * @param channel - Received message channel
     * @param message - Received message
     */
    void receive(String channel, String message);

}
