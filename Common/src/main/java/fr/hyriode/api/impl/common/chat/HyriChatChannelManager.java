package fr.hyriode.api.impl.common.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.*;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HyriChatChannelManager implements IHyriChatChannelManager {

    private final Map<HyriChatChannel, IHyriChatChannelHandler> handlers = new HashMap<>();

    public HyriChatChannelManager() {
        HyriAPI.get().getPubSub().subscribe(HyriChannel.CHAT, new Receiver());
    }

    @Override
    public void registerHandler(@NotNull HyriChatChannel channel, @NotNull IHyriChatChannelHandler handler) {
        this.handlers.put(channel, handler);
    }

    @Override
    public void unregisterHandler(@NotNull HyriChatChannel channel) {
        this.handlers.remove(channel);
    }

    @Override
    public IHyriChatChannelHandler getHandler(@NotNull HyriChatChannel channel) {
        return this.handlers.get(channel);
    }

    @Override
    public void sendMessage(@NotNull HyriChatChannel channel, @NotNull UUID sender, @NotNull String message, boolean component) {
        final ChatChannelMessageEvent event = new ChatChannelMessageEvent(channel, sender, message, component);

        HyriAPI.get().getEventBus().publish(event);

        if (event.isCancelled()) {
            return;
        }

        if (!channel.isAcrossNetwork()) {
            final IHyriChatChannelHandler handler = this.getHandler(channel);

            if (handler != null) {
                handler.onMessage(sender, message, component);
            }
            return;
        }

        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new ChatChannelPacket(channel, sender, message, component));
    }

    public class Receiver implements IHyriPacketReceiver {

        @Override
        public void receive(String channel, HyriPacket packet) {
            if (packet instanceof ChatChannelPacket) {
                final ChatChannelPacket channelPacket = (ChatChannelPacket) packet;
                final IHyriChatChannelHandler handler = getHandler(channelPacket.getChannel());

                if (handler != null) {
                    handler.onMessage(channelPacket.getSender(), channelPacket.getMessage(), channelPacket.isComponent());
                }
            }
        }

    }

}
