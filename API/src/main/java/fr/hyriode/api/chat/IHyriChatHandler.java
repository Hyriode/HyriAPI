package fr.hyriode.api.chat;

import fr.hyriode.api.rank.EHyriRank;

import java.util.UUID;

public interface IHyriChatHandler {

    String getChannel();

    EHyriRank getRequiredRank();

    String getPrefix();

    void onMessage(String channel, String message, UUID sender);

    void onMessageToPlayer(String channel, UUID receiver, String message, UUID sender);
}
