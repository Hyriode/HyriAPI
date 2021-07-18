package fr.hyriode.hyriapi.implementation.hyggdrasil.task;

import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.packet.common.HeartbeatPacket;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.implementation.hyggdrasil.HyggdrasilManager;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 19:06
 */
public class HeartbeatTask implements Runnable {

    private final HyggdrasilManager hyggdrasilManager;

    public HeartbeatTask(HyggdrasilManager hyggdrasilManager) {
        this.hyggdrasilManager = hyggdrasilManager;
    }

    @Override
    public void run() {
        final HeartbeatPacket heartbeatPacket = new HeartbeatPacket(HyriAPI.get().getServer().getId(), HeartbeatPacket.ApplicationType.HYGGDRASIL_CLIENT);

        this.hyggdrasilManager.sendPacket(HyggdrasilChannel.SERVERS, heartbeatPacket);

        if (System.currentTimeMillis() - this.hyggdrasilManager.getLastClientHeartbeat() >= 20 * 1000L) {
            System.err.println("Couldn't not contact the HyggdrasilClient !");
            this.hyggdrasilManager.setConnected(false);
        }
    }

}
