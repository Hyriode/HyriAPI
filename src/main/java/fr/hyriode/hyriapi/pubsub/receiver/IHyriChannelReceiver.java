package fr.hyriode.hyriapi.pubsub.receiver;

/**
 * Project: TestProject
 * Created by AstFaster
 * on 04/11/2021 at 10:34
 */
@FunctionalInterface
public interface IHyriChannelReceiver {

    /**
     * Called when a message is received on Redis PubSub
     *
     * @param channel - Received message channel
     * @param message - Received message
     */
    void receive(String channel, String message);

}
