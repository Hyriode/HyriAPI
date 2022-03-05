package fr.hyriode.api.impl.common.pubsub;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
class HyriPubSubMessage {

    private final String channel;
    private final String message;
    private final Runnable callback;

    public HyriPubSubMessage(String channel, String message, Runnable callback) {
        this.channel = channel;
        this.message = message;
        this.callback = callback;
    }

    public HyriPubSubMessage(String channel, String message) {
        this(channel, message, null);
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }

    public Runnable getCallback() {
        return this.callback;
    }

}
