package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.impl.common.player.packet.HyriPlayerKickPacket;
import fr.hyriode.api.impl.common.player.title.PlayerTitlePacket;
import fr.hyriode.api.impl.common.player.title.TitlePacket;
import fr.hyriode.api.impl.proxy.player.HyriPlayerManager;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.packet.model.HyriSendPlayerPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 10:10
 */
public class HyriPlayerReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof PlayerTitlePacket) {
            final PlayerTitlePacket titlePacket = (PlayerTitlePacket) packet;
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(titlePacket.getPlayerId());

            if (player != null) {
                HyriPlayerManager.sendTitleToPlayer(player, titlePacket.getTitle(), titlePacket.getSubtitle(), titlePacket.getFadeIn(), titlePacket.getStay(), titlePacket.getFadeOut());
            }
        } else if (packet instanceof TitlePacket) {
            final TitlePacket titlePacket = (TitlePacket) packet;

            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                HyriPlayerManager.sendTitleToPlayer(player, titlePacket.getTitle(), titlePacket.getSubtitle(), titlePacket.getFadeIn(), titlePacket.getStay(), titlePacket.getFadeOut());
            }
        } else if (packet instanceof HyriSendPlayerPacket) {
            final HyriSendPlayerPacket sendPacket = (HyriSendPlayerPacket) packet;

            HyriProxyReceiver.connectPlayer(sendPacket.getPlayerUUID(), sendPacket.getServerName());
        } else if (packet instanceof HyriPlayerKickPacket) {
            final HyriPlayerKickPacket kickPacket = (HyriPlayerKickPacket) packet;
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(kickPacket.getPlayerId());

            if (player != null) {
                player.disconnect(MessageUtil.deserializeComponent(kickPacket.getComponent()));
            }
        }
    }

}
