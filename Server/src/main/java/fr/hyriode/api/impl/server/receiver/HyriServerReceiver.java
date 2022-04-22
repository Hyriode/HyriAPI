package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.request.HyggRequestHeader;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.protocol.response.IHyggResponse;
import fr.hyriode.hyggdrasil.api.server.packet.HyggStopServerPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 23:13
 */
public class HyriServerReceiver implements IHyggPacketReceiver {

    @Override
    public IHyggResponse receive(String channel, HyggPacket packet, HyggRequestHeader header) {
        if (packet instanceof HyggStopServerPacket) {
            final String serverName = ((HyggStopServerPacket) packet).getServerName();
            final IHyriServer server = HyriAPI.get().getServer();

            if (server.getName().equals(serverName)) {
                server.getStopHandler().run();

                return HyggResponse.Type.SUCCESS;
            }
            return HyggResponse.Type.NONE;
        }
        return HyggResponse.Type.NONE;
    }

}
