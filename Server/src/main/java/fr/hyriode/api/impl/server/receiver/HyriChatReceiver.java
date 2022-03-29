package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.IHyriChatManager;
import fr.hyriode.api.chat.packet.MessageAcrossNetworkPacket;
import fr.hyriode.api.chat.packet.PlayerMessageAcrossNetworkPacket;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.player.IHyriPlayerManager;

public class HyriChatReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        final IHyriChatManager chatManager = HyriAPI.get().getChatManager();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        if (packet instanceof MessageAcrossNetworkPacket) {
            final MessageAcrossNetworkPacket message = (MessageAcrossNetworkPacket) packet;

            chatManager.getHandler(message.getChannel()).onMessage(message.getChannel(), message.getMessage(), message.getSender());
        } else if (packet instanceof PlayerMessageAcrossNetworkPacket) {
            final PlayerMessageAcrossNetworkPacket message = (PlayerMessageAcrossNetworkPacket) packet;
            if (HyriAPI.get().getServer().getName().equals(playerManager.getPlayer(message.getPlayer()).getCurrentServer())) {
                chatManager.getHandler(message.getChannel()).onMessageToPlayer(message.getChannel(), message.getPlayer(), message.getMessage(), message.getSender());
            }
        }
    }
}