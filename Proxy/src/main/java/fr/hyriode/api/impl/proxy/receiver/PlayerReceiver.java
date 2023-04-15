package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.impl.common.player.packet.PlayerKickPacket;
import fr.hyriode.api.impl.common.player.packet.PlayerTitlePacket;
import fr.hyriode.api.impl.common.player.packet.TitlePacket;
import fr.hyriode.api.impl.proxy.player.PHyriPlayerManager;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.packet.model.PlayerLimboSendPacket;
import fr.hyriode.api.packet.model.PlayerServerSendPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 10:10
 */
public class PlayerReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof PlayerTitlePacket) {
            final PlayerTitlePacket titlePacket = (PlayerTitlePacket) packet;
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(titlePacket.getPlayerId());

            if (player != null) {
                PHyriPlayerManager.sendTitleToPlayer(player, titlePacket.getTitle(), titlePacket.getSubtitle(), titlePacket.getFadeIn(), titlePacket.getStay(), titlePacket.getFadeOut());
            }
        } else if (packet instanceof TitlePacket) {
            final TitlePacket titlePacket = (TitlePacket) packet;

            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                PHyriPlayerManager.sendTitleToPlayer(player, titlePacket.getTitle(), titlePacket.getSubtitle(), titlePacket.getFadeIn(), titlePacket.getStay(), titlePacket.getFadeOut());
            }
        } else if (packet instanceof PlayerServerSendPacket) {
            final PlayerServerSendPacket sendPacket = (PlayerServerSendPacket) packet;

            this.connectPlayer(sendPacket.getPlayerUUID(), sendPacket.getServerName());
        } else if (packet instanceof PlayerLimboSendPacket) {
            final PlayerLimboSendPacket sendPacket = (PlayerLimboSendPacket) packet;

            this.connectPlayer(sendPacket.getPlayerId(), sendPacket.getLimboName());
        } else if (packet instanceof PlayerKickPacket) {
            final PlayerKickPacket kickPacket = (PlayerKickPacket) packet;
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(kickPacket.getPlayerId());

            if (player != null) {
                player.disconnect(ComponentSerializer.parse(kickPacket.getComponent()));
            }
        }
    }

    private void connectPlayer(UUID playerId, String server) {
        final ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server);
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerId);

        if (player != null && serverInfo != null) {
            player.connect(serverInfo);
        }
    }

}
