package fr.hyriode.api.impl.common.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.ChatChannelComponentPacket;
import fr.hyriode.api.chat.channel.IHyriChatChannelHandler;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import fr.hyriode.api.chat.channel.ChatChannelMessagePacket;
import fr.hyriode.api.packet.HyriChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HyriChatChannelManager implements IHyriChatChannelManager {

    private final Map<String, IHyriChatChannelHandler> handlers;

    public HyriChatChannelManager() {
        this.handlers = new HashMap<>();
    }

    @Override
    public IHyriChatChannelHandler getHandler(String channel) {
        return this.handlers.get(channel);
    }

    @Override
    public void registerChannel(IHyriChatChannelHandler handler) {
        this.handlers.put(handler.getChannel(), handler);
    }

    @Override
    public void unregisterChannel(String channel) {
        this.handlers.remove(channel);
    }

    @Override
    public void sendMessage(String channel, String message, UUID sender, boolean force) {
        final IHyriChatChannelHandler handler = this.getHandler(channel);

        if (handler != null && !handler.isAcrossNetwork()) {
            handler.onMessage(channel, message, sender, force);
            return;
        }

        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new ChatChannelMessagePacket(channel, message, sender, force));
    }

    @Override
    public void sendComponent(String channel, String component, boolean force) {
        final IHyriChatChannelHandler handler = this.getHandler(channel);

        if (handler != null && !handler.isAcrossNetwork()) {
            handler.onComponent(channel, component, force);
            return;
        }

        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new ChatChannelComponentPacket(channel, component, force));
    }

}
