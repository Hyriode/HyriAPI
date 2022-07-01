package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.HyriAPIImplementation;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.request.HyggRequestHeader;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.protocol.response.IHyggResponse;
import fr.hyriode.hyggdrasil.api.queue.packet.group.HyggQueueTransferGroupPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.player.HyggQueueTransferPlayerPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 15:17
 */
public class HyriQueueReceiver implements IHyggPacketReceiver {

    private final HyriAPIImplementation api;

    public HyriQueueReceiver(HyriAPIImplementation api) {
        this.api = api;
    }

    @Override
    public IHyggResponse receive(String channel, HyggPacket packet, HyggRequestHeader header) {
        if (packet instanceof HyggQueueTransferGroupPacket) {
            final HyggQueueTransferGroupPacket transferPacket = (HyggQueueTransferGroupPacket) packet;
            final IHyriParty party = HyriAPI.get().getPartyManager().getParty(transferPacket.getGroupId());

            if (party == null) {
                return HyggResponse.Type.ABORT;
            }

            final ProxiedPlayer leader = ProxyServer.getInstance().getPlayer(party.getLeader());

            if (leader != null) {
                this.api.getQueueManager().removePartyQueue(party.getId());

                HyriAPI.get().getServerManager().sendPartyToServer(transferPacket.getGroupId(), transferPacket.getServerName());
            }

            return HyggResponse.Type.SUCCESS;
        } else if (packet instanceof HyggQueueTransferPlayerPacket) {
            final HyggQueueTransferPlayerPacket transferPacket = (HyggQueueTransferPlayerPacket) packet;
            final UUID playerId = transferPacket.getPlayerId();
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerId);

            if (player != null) {
                this.api.getQueueManager().removePlayerQueue(playerId);

                HyriAPI.get().getServerManager().sendPlayerToServer(transferPacket.getPlayerId(), transferPacket.getServerName());
            }
            return HyggResponse.Type.SUCCESS;
        }
        return HyggResponse.Type.NONE;
    }

}

