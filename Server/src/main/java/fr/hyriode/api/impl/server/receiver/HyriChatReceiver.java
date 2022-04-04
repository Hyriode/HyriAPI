package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.chat.IHyriChatChannelManager;
import fr.hyriode.api.chat.packet.ChatMessagePacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.player.IHyriPlayerManager;

public class HyriChatReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        final IHyriChatChannelManager chat = HyriAPI.get().getChatChannelManager();
        final IHyriPlayerManager player = HyriAPI.get().getPlayerManager();

        if (packet instanceof ChatMessagePacket) {
            final ChatMessagePacket message = (ChatMessagePacket) packet;

            final IHyriChatChannelHandler handler = chat.getHandler(channel);

            if (handler == null) {
                return;
            }

            handler.onMessage(message.getChannel(), message.getMessage(), message.getSender(), message.isForce());
        } else if (packet instanceof PlayerMessagePacket) {
            final PlayerMessagePacket message = (PlayerMessagePacket) packet;

            final IHyriChatChannelHandler handler = chat.getHandler(message.getChannel());

            if (handler == null) {
                return;
            }

            if (!HyriAPI.get().getServer().getName().equals(player.getPlayer(message.getPlayer()).getCurrentServer())) {
                return;
            }

            handler.onMessageToPlayer(message.getChannel(), message.getPlayer(), message.getMessage(), message.getSender(), message.isForce());
        }
    }
}
