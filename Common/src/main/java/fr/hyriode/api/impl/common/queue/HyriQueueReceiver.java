package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.hylios.api.queue.packet.QueueInfoPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:18
 */
public class HyriQueueReceiver implements IHyriPacketReceiver {

    private final HyriQueueManager queueManager;

    public HyriQueueReceiver(HyriQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof QueueInfoPacket) {
            for (IHyriQueueHandler handler : this.queueManager.getHandlers()) {
                handler.onQueueInfo((QueueInfoPacket) packet);
            }
        }
    }

}
