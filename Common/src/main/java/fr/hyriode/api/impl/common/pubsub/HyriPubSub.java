package fr.hyriode.api.impl.common.pubsub;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.redis.HyriRedisConnection;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.api.redis.IHyriRedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPubSub implements IHyriPubSub {

    private static final String CHANNEL_PREFIX = "Hyriode@";

    private Thread senderThread;
    private Thread subscriberThread;

    private boolean running;

    private final Sender sender;
    private final HyriPubSubSubscriber subscriber;

    private final IHyriRedisConnection redisConnection;

    public HyriPubSub() {
        this.redisConnection = HyriAPI.get().getRedisConnection().clone();
        this.sender = new Sender();
        this.subscriber = new HyriPubSubSubscriber();

        this.start();
    }

    private void start() {
        HyriCommonImplementation.log("Starting Redis PubSub...");

        this.running = true;

        this.senderThread = new Thread(sender, "SenderThread");
        this.senderThread.start();

        this.subscriberThread = new Thread(() -> {
            while (this.running) {
                try (final Jedis jedis = this.redisConnection.getResource()) {
                    jedis.psubscribe(this.subscriber, CHANNEL_PREFIX + "*");
                }
            }
        });
        this.subscriberThread.start();
    }

    public void stop() {
        HyriCommonImplementation.log("Stopping Redis PubSub...");

        this.sender.running = this.running = false;

        if (this.subscriber.isSubscribed()) {
            this.subscriber.punsubscribe();
        }

        this.subscriberThread.interrupt();
        this.senderThread.interrupt();
    }

    @Override
    public void subscribe(String channel, IHyriPacketReceiver receiver) {
        HyriCommonImplementation.log("Subscribing '" + receiver.getClass().getSimpleName() + "' on " + channel);

        this.subscriber.registerReceiver(CHANNEL_PREFIX + channel, receiver);
    }

    @Override
    public void send(String channel, HyriPacket packet, Runnable callback) {
        this.sender.send(new HyriPubSubMessage(CHANNEL_PREFIX + channel, HyriAPI.GSON.toJson(packet), callback));
    }

    @Override
    public void send(String channel, HyriPacket packet) {
        this.send(channel, packet, null);
    }

    private class Sender implements Runnable {

        private final LinkedBlockingQueue<HyriPubSubMessage> messages = new LinkedBlockingQueue<>();

        private boolean running = true;

        private Jedis jedis;

        public void send(HyriPubSubMessage message) {
            this.messages.add(message);
        }

        @Override
        public void run() {
            this.check();

            while (this.running) {
                try {
                    this.process(this.messages.take());
                } catch (InterruptedException e) {
                    this.jedis.close();
                    return;
                }
            }
        }

        private void process(HyriPubSubMessage message) {
            boolean published = false;

            while (!published) {
                try {
                    final Runnable callback = message.getCallback();

                    this.jedis.publish(message.getChannel(), message.getMessage());

                    if (callback != null) {
                        callback.run();
                    }

                    published = true;
                } catch (Exception e) {
                    this.check();
                }
            }
        }

        private void check() {
            try {
                this.jedis = redisConnection.getResource();
            } catch (Exception e) {
                HyriCommonImplementation.log(Level.SEVERE, "[" + this.getClass().getSimpleName() + "] Couldn't connect to Redis server. Error: " + e.getMessage() + ". Recheck in 5 seconds.");

                try {
                    Thread.sleep(5000);

                    this.check();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private static class HyriPubSubSubscriber extends JedisPubSub {

        private final Map<String, Set<IHyriPacketReceiver>> receivers = new HashMap<>();

        public void registerReceiver(String channel, IHyriPacketReceiver receiver) {
            final Set<IHyriPacketReceiver> receivers = this.receivers.get(channel) != null ? this.receivers.get(channel) : new HashSet<>();

            receivers.add(receiver);

            this.receivers.put(channel, receivers);
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            final HyriPacket packet = HyriAPI.GSON.fromJson(message, HyriPacket.class);
            final Set<IHyriPacketReceiver> receivers = this.receivers.get(channel);

            if (receivers != null) {
                receivers.forEach(receiver -> receiver.receive(channel, packet));
            }
        }

    }

}
