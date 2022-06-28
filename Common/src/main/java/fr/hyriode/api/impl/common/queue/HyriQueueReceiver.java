package fr.hyriode.api.impl.common.queue;

import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.request.HyggRequestHeader;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.protocol.response.IHyggResponse;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueInfoPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:18
 */
public class HyriQueueReceiver implements IHyggPacketReceiver {

    private final HyriQueueManager queueManager;

    public HyriQueueReceiver(HyriQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Override
    public IHyggResponse receive(String channel, HyggPacket packet, HyggRequestHeader header) {
        if (packet instanceof HyggQueueInfoPacket) {
            this.queueManager.onQueueInfo((HyggQueueInfoPacket) packet);

            return HyggResponse.Type.SUCCESS;
        }
        return HyggResponse.Type.NONE;
    }

}
