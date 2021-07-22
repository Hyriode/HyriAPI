package fr.hyriode.hyriapi.impl.api.server;

import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.event.HyggdrasilEventHandler;
import fr.hyriode.hyggdrasilconnector.protocol.event.HyggdrasilPacketListener;
import fr.hyriode.hyggdrasilconnector.protocol.event.packet.PacketReceiveEvent;
import fr.hyriode.hyggdrasilconnector.protocol.packet.HyggdrasilPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.server.ServerListPacket;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriPlugin;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 14:43
 */
public class HyriServerPacketHandler extends HyggdrasilPacketListener {

    private String channel;

    public HyriServerPacketHandler(HyriPlugin plugin) {
        this.channel = HyggdrasilChannel.SERVERS.getName();

        plugin.getHyggdrasilManager().getHyggdrasilConnector().getEventManager().registerListener(this);
    }

    @HyggdrasilEventHandler
    public void onPacket(PacketReceiveEvent event) {
        final HyriServerManager serverManager = (HyriServerManager) HyriAPI.get().getServerManager();
        final HyggdrasilPacket packet = event.getPacket();

        if (packet instanceof ServerListPacket) {
            serverManager.setServers(((ServerListPacket) packet).getServers());
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
