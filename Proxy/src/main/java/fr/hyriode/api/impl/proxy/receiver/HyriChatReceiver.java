package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.chat.packet.BroadcastPacket;
import fr.hyriode.api.chat.packet.ComponentPacket;
import fr.hyriode.api.chat.packet.PlayerComponentPacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 08:51
 */
public class HyriChatReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof PlayerComponentPacket) {
            final PlayerComponentPacket componentPacket = (PlayerComponentPacket) packet;

            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(componentPacket.getPlayer());

            if (player != null) {
                player.sendMessage(deserializeComponent(componentPacket.getComponent()));
            }
        } else if (packet instanceof ComponentPacket) {
            final TextComponent component = deserializeComponent(((ComponentPacket) packet).getComponent());

            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage(component);
            }
        } else if (packet instanceof PlayerMessagePacket) {
            final PlayerMessagePacket message = (PlayerMessagePacket) packet;
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(message.getPlayer());

            if (player != null) {
                player.sendMessage(TextComponent.fromLegacyText(message.getMessage()));
            }
        }
    }

    public static TextComponent deserializeComponent(String json) {
        return new TextComponent(ComponentSerializer.parse(json));
    }

}
