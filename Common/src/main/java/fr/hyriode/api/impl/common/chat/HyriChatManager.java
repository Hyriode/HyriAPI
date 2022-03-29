package fr.hyriode.api.impl.common.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.IHyriChatHandler;
import fr.hyriode.api.chat.IHyriChatManager;
import fr.hyriode.api.chat.packet.MessageAcrossNetworkPacket;
import fr.hyriode.api.chat.packet.PlayerMessageAcrossNetworkPacket;

import java.util.*;

public class HyriChatManager implements IHyriChatManager {

    private final Map<String, IHyriChatHandler> handlers;

    public HyriChatManager() {
        this.handlers = new HashMap<>();
    }

    @Override
    public IHyriChatHandler getHandler(String channel) {
        return this.handlers.get(channel);
    }

    @Override
    public void registerChannel(String channel, IHyriChatHandler handler) {
        this.handlers.put(channel, handler);
    }

    @Override
    public void unregisterChannel(String channel) {
        this.handlers.remove(channel);
    }

    @Override
    public void sendMessageAcrossNetwork(String channel, String message) {
        HyriAPI.get().getPubSub().send(IHyriChatManager.PUBSUB_CHANNEL, new MessageAcrossNetworkPacket(channel, message));
    }

    @Override
    public void sendMessageAcrossNetwork(String channel, String message, Runnable callback) {
        HyriAPI.get().getPubSub().send(IHyriChatManager.PUBSUB_CHANNEL, new MessageAcrossNetworkPacket(channel, message), callback);
    }

    @Override
    public void sendMessageToPlayerAcrossNetwork(String channel, UUID player, String message) {
        HyriAPI.get().getPubSub().send(IHyriChatManager.PUBSUB_CHANNEL, new PlayerMessageAcrossNetworkPacket(player, channel, message));
    }

    @Override
    public void sendMessageToPlayerAcrossNetwork(String channel, UUID player, String message, Runnable callback) {
        HyriAPI.get().getPubSub().send(IHyriChatManager.PUBSUB_CHANNEL, new PlayerMessageAcrossNetworkPacket(player, channel, message), callback);
    }
}
