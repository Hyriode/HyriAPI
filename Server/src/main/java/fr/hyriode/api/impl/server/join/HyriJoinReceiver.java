package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.server.join.packet.HyriPartyJoinPacket;
import fr.hyriode.api.server.join.packet.HyriPlayerJoinPacket;

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
        if (packet instanceof HyriPlayerJoinPacket) {
            final HyriPlayerJoinPacket joinPacket = (HyriPlayerJoinPacket) packet;

            if (joinPacket.getTargetServer().equals(HyriAPI.get().getServer().getName())) {
                this.joinManager.requestPlayerJoin(joinPacket.getPlayerId(), true);
            }
        } else if (packet instanceof HyriPartyJoinPacket) {
            final HyriPartyJoinPacket joinPacket = (HyriPartyJoinPacket) packet;

            if (joinPacket.getTargetServer().equals(HyriAPI.get().getServer().getName())) {
                this.joinManager.requestPartyJoin(joinPacket.getPartyId());
            }
        }
    }

}
