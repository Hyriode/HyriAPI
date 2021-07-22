package fr.hyriode.hyriapi.impl.api.server;

import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.packet.server.ServerAskListPacket;
import fr.hyriode.hyriapi.impl.HyriPlugin;
import fr.hyriode.hyriapi.impl.hyggdrasil.HyggdrasilManager;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 15:55
 */
public class HyriServerPacketTask implements Runnable {

    private final HyriPlugin plugin;

    public HyriServerPacketTask(HyriPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        final HyggdrasilManager hyggdrasilManager = plugin.getHyggdrasilManager();

        this.sendAskServerList(hyggdrasilManager);
    }

    private void sendAskServerList(HyggdrasilManager hyggdrasilManager) {
        final ServerAskListPacket packet = new ServerAskListPacket(HyggdrasilChannel.SERVERS);

        hyggdrasilManager.sendPacket(HyggdrasilChannel.QUERY, packet);
    }

}
