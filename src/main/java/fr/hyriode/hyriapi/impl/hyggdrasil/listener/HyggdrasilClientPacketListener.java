package fr.hyriode.hyriapi.impl.hyggdrasil.listener;

import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.event.HyggdrasilEventHandler;
import fr.hyriode.hyggdrasilconnector.protocol.event.HyggdrasilPacketListener;
import fr.hyriode.hyggdrasilconnector.protocol.event.packet.PacketReceiveEvent;
import fr.hyriode.hyggdrasilconnector.protocol.packet.HyggdrasilPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.common.HeartbeatPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.server.ServerAskInfoPacket;
import fr.hyriode.hyriapi.impl.hyggdrasil.HyggdrasilManager;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 19:58
 */
public class HyggdrasilClientPacketListener extends HyggdrasilPacketListener {

    private String channel;

    private final HyggdrasilManager hyggdrasilManager;

    public HyggdrasilClientPacketListener(HyggdrasilManager hyggdrasilManager) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.channel = HyggdrasilChannel.SERVERS.getName();
    }

    @HyggdrasilEventHandler
    public void onPacket(PacketReceiveEvent event) {
        final HyggdrasilPacket packet = event.getPacket();

        if (packet instanceof HeartbeatPacket) {
            this.hyggdrasilManager.heartbeat((HeartbeatPacket) packet);
        } else if (packet instanceof ServerAskInfoPacket) {
            this.hyggdrasilManager.askInfo((ServerAskInfoPacket) packet);
        }
    }

    @Override
    public String getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

}
