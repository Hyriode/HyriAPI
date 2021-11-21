package fr.hyriode.hyriapi.pubsub.receiver;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 04/11/2021 at 13:54
 */
public interface IHyriPatternReceiver {

    /**
     * Called when a message is received on Redis PubSub
     *
     * @param pattern - Received message pattern
     * @param channel - Received message channel
     * @param message - Received message
     */
    void receive(String pattern, String channel, String message);

}
