package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.ChatChannelComponentPacket;
import fr.hyriode.api.chat.channel.ChatChannelMessagePacket;
import fr.hyriode.api.chat.channel.IHyriChatChannelHandler;
import fr.hyriode.api.chat.packet.BroadcastPacket;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class HyriChatReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof BroadcastPacket) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 0.1F);
            }
        } else if (packet instanceof ChatChannelMessagePacket) {
            final ChatChannelMessagePacket message = (ChatChannelMessagePacket) packet;
            final IHyriChatChannelHandler handler = HyriAPI.get().getChatChannelManager().getHandler(message.getChannel());

            if (handler == null) {
                return;
            }

            handler.onMessage(message.getChannel(), message.getMessage(), message.getSender(), message.isForce());
        } else if (packet instanceof ChatChannelComponentPacket) {
            final ChatChannelComponentPacket component = (ChatChannelComponentPacket) packet;
            final IHyriChatChannelHandler handler = HyriAPI.get().getChatChannelManager().getHandler(component.getChannel());

            if (handler == null) {
                return;
            }

            handler.onComponent(component.getChannel(), component.getComponent(), component.isForce());
        }
    }
}
