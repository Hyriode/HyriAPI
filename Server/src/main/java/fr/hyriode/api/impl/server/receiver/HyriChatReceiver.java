package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.chat.packet.ChatChannelMessagePacket;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;

public class HyriChatReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof ChatChannelMessagePacket) {
            final ChatChannelMessagePacket message = (ChatChannelMessagePacket) packet;
            final IHyriChatChannelHandler handler = HyriAPI.get().getChatChannelManager().getHandler(message.getChannel());

            if (handler == null) {
                return;
            }

            handler.onMessage(message.getChannel(), message.getMessage(), message.getSender(), message.isForce());
        }
    }
}
