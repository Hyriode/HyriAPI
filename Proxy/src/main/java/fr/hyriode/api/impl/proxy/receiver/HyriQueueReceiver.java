package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.request.HyggRequestHeader;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.protocol.response.IHyggResponse;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueTransferPacket;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 15:17
 */
public class HyriQueueReceiver implements IHyggPacketReceiver {

    @Override
    public IHyggResponse receive(String channel, HyggPacket packet, HyggRequestHeader header) {
        if (packet instanceof HyggQueueTransferPacket) {
            final HyggQueueTransferPacket transferPacket = (HyggQueueTransferPacket) packet;
            final UUID playerId = transferPacket.getPlayerId();

            HyriAPI.get().getServerManager().sendPartyToServer(playerId, transferPacket.getServerName());
        }
        return HyggResponse.Type.NONE;
    }
}

