package fr.hyriode.hyriapi.impl.pubsub;

import com.google.gson.Gson;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.pubsub.HyriPacket;
import fr.hyriode.hyriapi.pubsub.IHyriPubSub;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriChannelPacketReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriChannelReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriPatternPacketReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriPatternReceiver;
import redis.clients.jedis.Jedis;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPubSub implements IHyriPubSub {

    private Thread senderThread;
    private Thread subscriberThread;

    private boolean running;

    private final HyriPubSubSender sender;
    private final HyriPubSubSubscriber subscriber;

    public HyriPubSub() {
        this.sender = new HyriPubSubSender();
        this.subscriber = new HyriPubSubSubscriber();

        this.start();
    }

    private void start() {
        this.running = true;

        this.senderThread = new Thread(sender, "SenderThread");
        this.senderThread.start();

        this.subscriberThread = new Thread(() -> {
            while (this.running) {
                final Jedis jedis = HyriAPI.get().getRedisResource();
                final String[] channels = this.subscriber.getChannelsSubscribed().toArray(new String[0]);

                if (channels.length > 0) {
                    jedis.subscribe(this.subscriber, channels);
                }

                final String[] patterns = this.subscriber.getPatternsSubscribed().toArray(new String[0]);

                if (patterns.length > 0) {
                    jedis.psubscribe(this.subscriber, patterns);
                }

                jedis.close();
            }
        });
        this.subscriberThread.start();
    }

    public void stop() {
        this.running = false;

        this.sender.setRunning(this.running);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.unsubscribe();
            this.subscriber.punsubscribe();
        }

        this.subscriberThread.interrupt();
        this.senderThread.interrupt();
    }

    @Override
    public void subscribe(String channel, IHyriChannelReceiver receiver) {
        this.subscriber.registerReceiver(channel, receiver);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.unsubscribe();
        }
    }

    @Override
    public void subscribe(String channel, IHyriChannelPacketReceiver receiver) {
        this.subscriber.registerReceiver(channel, receiver);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.unsubscribe();
        }
    }

    @Override
    public void subscribe(String pattern, IHyriPatternReceiver receiver) {
        this.subscriber.registerReceiver(pattern, receiver);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.punsubscribe();
        }
    }

    @Override
    public void subscribe(String pattern, IHyriPatternPacketReceiver receiver) {
        this.subscriber.registerReceiver(pattern, receiver);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.punsubscribe();
        }
    }

    @Override
    public void send(String channel, String message, Runnable callback) {
        this.sender.send(new HyriPubSubMessage(channel, message, callback));
    }

    @Override
    public void send(String channel, String message) {
        this.send(channel, message, null);
    }

    @Override
    public void send(String channel, HyriPacket packet, Runnable callback) {
        this.send(channel, new Gson().toJson(packet), callback);
    }

    @Override
    public void send(String channel, HyriPacket packet) {
        this.send(channel, packet, null);
    }

}
