package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.chat.packet.BroadcastMessagePacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 08:51
 */
public class ChatReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
       if (packet instanceof BroadcastMessagePacket) {
            final BroadcastMessagePacket messagePacket = (BroadcastMessagePacket) packet;
            final BaseComponent[] component = messagePacket.isComponent() ? ComponentSerializer.parse(messagePacket.getMessage()) : TextComponent.fromLegacyText(messagePacket.getMessage());

            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage(component);
            }
        } else if (packet instanceof PlayerMessagePacket) {
            final PlayerMessagePacket messagePacket = (PlayerMessagePacket) packet;
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(messagePacket.getPlayer());

            if (player != null) {
                player.sendMessage(messagePacket.isComponent() ? ComponentSerializer.parse(messagePacket.getMessage()) : TextComponent.fromLegacyText(messagePacket.getMessage()));
            }
        }
    }

}
