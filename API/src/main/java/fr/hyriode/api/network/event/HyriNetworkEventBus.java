package fr.hyriode.api.network.event;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.event.HyriEventBus;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 14:17
 */
public class HyriNetworkEventBus extends HyriEventBus implements IHyriPacketReceiver {

    private static final String CHANNEL = "events";

    public HyriNetworkEventBus() {
        super("network");

        HyriAPI.get().getPubSub().subscribe(CHANNEL, this);
    }

    @Override
    public void publish(HyriEvent event) {
        this.publish0(event, false);
    }

    @Override
    public void publishAsync(HyriEvent event) {
        this.publish0(event, true);
    }

    private void publish0(HyriEvent event, boolean async) {
        HyriAPI.get().getPubSub().send(CHANNEL, new HyriNetworkEventPacket(event.getClass(), event, async));
    }

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof HyriNetworkEventPacket) {
            final HyriNetworkEventPacket eventPacket = (HyriNetworkEventPacket) packet;
            final HyriEvent event = eventPacket.getEvent();

            if (eventPacket.isAsyncEvent()) {
                super.publishAsync(event);
            } else {
                super.publish(event);
            }
        }
    }

}
