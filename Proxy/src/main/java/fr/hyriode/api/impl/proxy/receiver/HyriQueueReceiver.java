package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.request.HyggRequestHeader;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.protocol.response.IHyggResponse;
import fr.hyriode.hyggdrasil.api.queue.packet.group.HyggQueueTransferGroupPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.player.HyggQueueTransferPlayerPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 15:17
 */
public class HyriQueueReceiver implements IHyggPacketReceiver {

    @Override
    public IHyggResponse receive(String channel, HyggPacket packet, HyggRequestHeader header) {
        if (packet instanceof HyggQueueTransferGroupPacket) {
            final HyggQueueTransferGroupPacket transferPacket = (HyggQueueTransferGroupPacket) packet;

            HyriAPI.get().getServerManager().sendPartyToServer(transferPacket.getGroupId(), transferPacket.getServerName());

            return HyggResponse.Type.SUCCESS;
        } else if (packet instanceof HyggQueueTransferPlayerPacket) {
            final HyggQueueTransferPlayerPacket transferPacket = (HyggQueueTransferPlayerPacket) packet;

            HyriAPI.get().getServerManager().sendPlayerToServer(transferPacket.getPlayerId(), transferPacket.getServerName());

            return HyggResponse.Type.SUCCESS;
        }
        return HyggResponse.Type.NONE;
    }

}

