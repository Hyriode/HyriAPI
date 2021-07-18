package fr.hyriode.hyriapi.implementation.hyggdrasil;

import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.event.HyggdrasilEventHandler;
import fr.hyriode.hyggdrasilconnector.protocol.event.HyggdrasilListener;
import fr.hyriode.hyggdrasilconnector.protocol.event.HyggdrasilPacketListener;
import fr.hyriode.hyggdrasilconnector.protocol.event.packet.PacketReceiveEvent;
import fr.hyriode.hyggdrasilconnector.protocol.packet.HyggdrasilPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 20:40
 */
public class HyggdrasilPacketResponse {

    private HyggdrasilPacket packet;
    private HyggdrasilListener listener;

    private final HyggdrasilManager hyggdrasilManager;

    public HyggdrasilPacketResponse(HyggdrasilManager hyggdrasilManager) {
        this.hyggdrasilManager = hyggdrasilManager;
    }

    public HyggdrasilPacket waitResponse(Class<? extends HyggdrasilPacket> packetClass, HyggdrasilChannel returnChannel) {
        this.listener = new HyggdrasilPacketListener() {

            private String channel;

            @HyggdrasilEventHandler
            public void onPacket(PacketReceiveEvent event) {
                final HyggdrasilPacket inPacket = event.getPacket();

                if (inPacket.getClass().equals(packetClass)) {
                    packet = inPacket;

                    hyggdrasilManager.getHyggdrasilConnector().getEventManager().unregisterListener(listener);
                }
            }

            @Override
            public String getChannel() {
                return this.channel = returnChannel.getName();
            }

            @Override
            public void setChannel(String channel) {
                this.channel = channel;
            }
        };

        this.hyggdrasilManager.getHyggdrasilConnector().getEventManager().registerListener(this.listener);

        return this.getPacket();
    }

    private HyggdrasilPacket getPacket() {
        if (this.packet == null) {
            return this.getPacket();
        }

        return this.packet;
    }

}
