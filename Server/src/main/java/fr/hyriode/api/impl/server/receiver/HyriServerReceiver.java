package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.packet.model.server.HyggStopServerPacket;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.request.HyggRequestHeader;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.protocol.response.IHyggResponse;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 23:13
 */
public class HyriServerReceiver implements IHyggPacketReceiver {

    @Override
    public IHyggResponse receive(String channel, HyggPacket packet, HyggRequestHeader header) {
        if (packet instanceof HyggStopServerPacket) {
            return HyggResponse.Type.SUCCESS;
        }
        return HyggResponse.Type.NONE;
    }

}
