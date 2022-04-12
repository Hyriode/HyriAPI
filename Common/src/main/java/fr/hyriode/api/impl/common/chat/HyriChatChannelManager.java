package fr.hyriode.api.impl.common.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.chat.IHyriChatChannelManager;
import fr.hyriode.api.chat.packet.ChatMessagePacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.player.IHyriPlayerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HyriChatChannelManager implements IHyriChatChannelManager {

    private final IHyriPlayerManager manager;
    private final Map<String, IHyriChatChannelHandler> handlers;

    public HyriChatChannelManager() {
        this.handlers = new HashMap<>();
        this.manager = HyriAPI.get().getPlayerManager();
    }

    @Override
    public IHyriChatChannelHandler getHandler(String channel) {
        return this.handlers.get(channel);
    }

    @Override
    public void registerChannel(IHyriChatChannelHandler handler) {
        this.handlers.put(handler.getChannel(), handler);
        System.out.println("Registered channel " + handler.getChannel());
    }

    @Override
    public void unregisterChannel(String channel) {
        this.handlers.remove(channel);
    }

    @Override
    public void sendMessage(String channel, String message, UUID sender, boolean force) {
        final IHyriChatChannelHandler handler = this.getHandler(channel);

        if (!handler.isAcrossNetwork()) {
            handler.onMessage(channel, message, sender, force);
            return;
        }

        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new ChatMessagePacket(channel, message, sender, force));
    }

    @Override
    public void sendMessageToPlayer(String channel, String message, UUID player, UUID sender, boolean force) {
        if (HyriAPI.get().isServer() && HyriAPI.get().getServer().getName().equals(manager.getPlayer(player).getCurrentServer())) {
            this.getHandler(channel).onMessageToPlayer(channel, player, message, sender, force);
            return;
        }

        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new PlayerMessagePacket(player, channel, message, sender, force));
    }
}
