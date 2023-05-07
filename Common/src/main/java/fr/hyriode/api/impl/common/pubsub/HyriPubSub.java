package fr.hyriode.api.impl.common.pubsub;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.packet.event.HyriPacketEvent;
import fr.hyriode.api.packet.event.HyriPacketReceiveEvent;
import fr.hyriode.api.packet.event.HyriPacketSendEvent;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.api.redis.IRedis;
import fr.hyriode.hylios.api.MetricsRedisKey;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPubSub implements IHyriPubSub {

    private static final String CHANNEL_PREFIX = "hyriode@";

    private Thread subscriberThread;

    private boolean running;

    private final Subscriber subscriber;

    private final IRedis redisConnection;

    private long sent;

    public HyriPubSub() {
        this.redisConnection = HyriAPI.get().getRedisConnection();
        this.subscriber = new Subscriber();

        this.start();
    }

    private void start() {
        HyriAPI.get().log("Starting Redis PubSub...");

        this.running = true;

        this.subscriberThread = new Thread(() -> {
            while (this.running) {
                HyriAPI.get().getRedisProcessor().process(jedis -> jedis.psubscribe(this.subscriber, CHANNEL_PREFIX + "*"));
            }
        });
        this.subscriberThread.start();

        HyriAPI.get().getScheduler().schedule(() -> {
            final String key = MetricsRedisKey.HYRIAPI_PACKETS.getKey();
            HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.incrBy(key, this.sent));

            this.sent = 0;
        }, 10, 10, TimeUnit.SECONDS);
    }

    public void stop() {
        HyriAPI.get().log("Stopping Redis PubSub...");

        final JedisPool pool = this.redisConnection.getPool();

        pool.close();
        pool.destroy();

        if (this.subscriber.isSubscribed()) {
            this.subscriber.punsubscribe();
        }

        this.subscriberThread.interrupt();
    }

    @Override
    public void subscribe(String channel, IHyriPacketReceiver receiver) {
        HyriAPI.get().log("Subscribing '" + receiver.getClass().getSimpleName() + "' on " + channel);

        this.subscriber.registerReceiver(CHANNEL_PREFIX + channel, receiver);
    }

    @Override
    public void send(String channel, HyriPacket packet, Runnable callback) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final HyriPacketEvent event = new HyriPacketSendEvent(packet, channel);

            HyriAPI.get().getEventBus().publish(event);

            if (event.isCancelled()) {
                return;
            }

            this.sent++;
            jedis.publish(CHANNEL_PREFIX + event.getChannel(), HyriAPI.GSON.toJson(event.getPacket()));
        });
    }

    @Override
    public void send(String channel, HyriPacket packet) {
        this.send(channel, packet, null);
    }

    public IRedis getRedisConnection() {
        return this.redisConnection;
    }

    private static class Subscriber extends JedisPubSub {

        private final Map<String, Set<IHyriPacketReceiver>> receivers = new HashMap<>();

        public void registerReceiver(String channel, IHyriPacketReceiver receiver) {
            final Set<IHyriPacketReceiver> receivers = this.receivers.get(channel) != null ? this.receivers.get(channel) : new HashSet<>();

            receivers.add(receiver);

            this.receivers.put(channel, receivers);
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            try {
                final HyriPacket packet = HyriAPI.GSON.fromJson(message, HyriPacket.class);

                if (packet == null) {
                    return;
                }

                final HyriPacketEvent event = new HyriPacketReceiveEvent(packet, channel);

                HyriAPI.get().getEventBus().publish(event);

                if (event.isCancelled() || event.getPacket() == null || event.getChannel() == null) {
                    return;
                }

                final Set<IHyriPacketReceiver> receivers = this.receivers.get(channel);

                if (receivers != null) {
                    receivers.forEach(receiver -> receiver.receive(event.getChannel(), event.getPacket()));
                }
            } catch (Exception ignored) {}
        }

    }

}
