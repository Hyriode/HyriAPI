package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.server.join.packet.PlayerJoinPacket;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:57
 */
public class JoinReceiver implements IHyriPacketReceiver {

    private final JoinManager joinManager;

    public JoinReceiver(JoinManager joinManager) {
        this.joinManager = joinManager;
    }

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof PlayerJoinPacket) {
            final PlayerJoinPacket joinPacket = (PlayerJoinPacket) packet;

            if (!joinPacket.getTargetServer().equals(HyriAPI.get().getServer().getName())) {
              return;
            }

            final UUID player = joinPacket.getPlayerId();
            final String message = this.joinManager.requestPlayerJoin(player, true);

            if (message != null) {
                HyriAPI.get().getPlayerManager().sendMessage(player, message);
            }
        }
    }

}
