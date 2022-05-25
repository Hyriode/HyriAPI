package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.server.join.packet.HyriJoinPacket;
import fr.hyriode.api.server.join.packet.HyriPartyJoinPacket;
import fr.hyriode.api.server.join.packet.HyriPlayerJoinPacket;
import fr.hyriode.api.server.join.packet.HyriPlayerReconnectPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:57
 */
public class HyriJoinReceiver implements IHyriPacketReceiver {

    private final HyriJoinManager joinManager;

    public HyriJoinReceiver(HyriJoinManager joinManager) {
        this.joinManager = joinManager;
    }

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof HyriJoinPacket) {
            if (!((HyriJoinPacket) packet).getTargetServer().equals(HyriAPI.get().getServer().getName())) {
                return;
            }

           if (packet instanceof HyriPlayerJoinPacket) {
                this.joinManager.requestPlayerJoin(((HyriPlayerJoinPacket) packet).getPlayerId(), true, packet instanceof HyriPlayerReconnectPacket);
            } else if (packet instanceof HyriPartyJoinPacket) {
                this.joinManager.requestPartyJoin(((HyriPartyJoinPacket) packet).getPartyId());
            }
        }
    }

}
