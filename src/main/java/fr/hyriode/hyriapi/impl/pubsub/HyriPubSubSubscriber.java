package fr.hyriode.hyriapi.impl.pubsub;

import com.google.gson.Gson;
import fr.hyriode.hyriapi.pubsub.HyriPacket;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriChannelPacketReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriChannelReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriPatternPacketReceiver;
import fr.hyriode.hyriapi.pubsub.receiver.IHyriPatternReceiver;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
class HyriPubSubSubscriber extends JedisPubSub {

    private final Map<String, Set<IHyriChannelReceiver>> channelsReceivers;
    private final Map<String, Set<IHyriChannelPacketReceiver>> channelsPacketsReceivers;
    private final Map<String, Set<IHyriPatternReceiver>> patternsReceivers;
    private final Map<String, Set<IHyriPatternPacketReceiver>> patternsPacketsReceivers;

    public HyriPubSubSubscriber() {
        this.channelsReceivers = new HashMap<>();
        this.channelsPacketsReceivers = new HashMap<>();
        this.patternsReceivers = new HashMap<>();
        this.patternsPacketsReceivers = new HashMap<>();
    }

    public void registerReceiver(String channel, IHyriChannelReceiver receiver) {
        final Set<IHyriChannelReceiver> receivers = this.channelsReceivers.get(channel) != null ? this.channelsReceivers.get(channel) : new HashSet<>();

        receivers.add(receiver);

        this.channelsReceivers.put(channel, receivers);
    }

    public void registerReceiver(String pattern, IHyriPatternReceiver receiver) {
        final Set<IHyriPatternReceiver> receivers = this.patternsReceivers.get(pattern) != null ? this.patternsReceivers.get(pattern) : new HashSet<>();

        receivers.add(receiver);

        this.patternsReceivers.put(pattern, receivers);
    }

    public void registerReceiver(String pattern, IHyriChannelPacketReceiver receiver) {
        final Set<IHyriChannelPacketReceiver> receivers = this.channelsPacketsReceivers.get(pattern) != null ? this.channelsPacketsReceivers.get(pattern) : new HashSet<>();

        receivers.add(receiver);

        this.channelsPacketsReceivers.put(pattern, receivers);
    }

    public void registerReceiver(String pattern, IHyriPatternPacketReceiver receiver) {
        final Set<IHyriPatternPacketReceiver> receivers = this.patternsPacketsReceivers.get(pattern) != null ? this.patternsPacketsReceivers.get(pattern) : new HashSet<>();

        receivers.add(receiver);

        this.patternsPacketsReceivers.put(pattern, receivers);
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
            final HyriPacket packet = new Gson().fromJson(message, HyriPacket.class);
            final Set<IHyriChannelPacketReceiver> receivers = this.channelsPacketsReceivers.get(channel);

            if (receivers != null) {
                packet.setJson(message);
                receivers.forEach(receiver -> receiver.receive(channel, packet));
            }
        } catch (Exception ignored) {}

        final Set<IHyriChannelReceiver> receivers = this.channelsReceivers.get(channel);

        if (receivers != null) {
            receivers.forEach(receiver -> receiver.receive(channel, message));
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        try {
            final HyriPacket packet = new Gson().fromJson(message, HyriPacket.class);
            final Set<IHyriPatternPacketReceiver> receivers = this.patternsPacketsReceivers.get(pattern);

            if (receivers != null) {
                packet.setJson(message);
                receivers.forEach(receiver -> receiver.receive(pattern, channel, packet));
            }
        } catch (Exception ignored) {}

        final Set<IHyriPatternReceiver> receivers = this.patternsReceivers.get(pattern);

        if (receivers != null) {
            receivers.forEach(receiver -> receiver.receive(pattern, channel, message));
        }
    }

    public Set<String> getChannelsSubscribed() {
        final Set<String> channelsSubscribed = new HashSet<>();

        channelsSubscribed.addAll(this.channelsReceivers.keySet());
        channelsSubscribed.addAll(this.channelsPacketsReceivers.keySet());

        return channelsSubscribed;
    }

    public Set<String> getPatternsSubscribed() {
        final Set<String> patternsSubscribed = new HashSet<>();

        patternsSubscribed.addAll(this.patternsReceivers.keySet());
        patternsSubscribed.addAll(this.patternsPacketsReceivers.keySet());

        return patternsSubscribed;
    }
    
}
