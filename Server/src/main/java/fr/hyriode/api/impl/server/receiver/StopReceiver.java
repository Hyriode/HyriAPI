package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketHeader;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.packet.HyggStopServerPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 23:13
 */
public class StopReceiver implements IHyggPacketReceiver {

    @Override
    public HyggResponse receive(String channel, HyggPacketHeader header, HyggPacket packet) {
        if (packet instanceof HyggStopServerPacket) {
            final String serverName = ((HyggStopServerPacket) packet).getServerName();
            final IHyriServer server = HyriAPI.get().getServer();

            if (server.getName().equals(serverName)) {
                server.setState(HyggServer.State.SHUTDOWN);

                return HyggResponse.Type.SUCCESS.toResponse();
            }
        }
        return HyggResponse.Type.NONE.toResponse();
    }

}
