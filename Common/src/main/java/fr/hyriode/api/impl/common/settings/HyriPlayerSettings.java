package fr.hyriode.api.impl.common.settings;

import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.api.settings.HyriPlayersVisibilityLevel;
import fr.hyriode.api.settings.HyriPrivateMessagesLevel;
import fr.hyriode.api.settings.IHyriPlayerSettings;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayerSettings implements IHyriPlayerSettings {

    private boolean partyRequestsEnabled;
    private boolean friendRequestsEnabled;
    private HyriPrivateMessagesLevel privateMessagesLevel;
    private HyriPlayersVisibilityLevel playersVisibilityLevel;
    private boolean globalChatMessagesEnabled;
    private boolean privateMessagesSoundEnabled;
    private HyriLanguage language;
    private String chatChannel;
    private boolean autoQueueEnabled;

    public HyriPlayerSettings() {
        this.partyRequestsEnabled = true;
        this.friendRequestsEnabled = true;
        this.privateMessagesLevel = HyriPrivateMessagesLevel.ALL;
        this.playersVisibilityLevel = HyriPlayersVisibilityLevel.ALL;
        this.globalChatMessagesEnabled = true;
        this.privateMessagesSoundEnabled = true;
        this.language = HyriLanguage.FR;
        this.chatChannel = "global";
    }

    @Override
    public boolean isPartyRequestsEnabled() {
        return this.partyRequestsEnabled;
    }

    @Override
    public void setPartyRequestsEnabled(boolean partyRequestsEnabled) {
        this.partyRequestsEnabled = partyRequestsEnabled;
    }

    @Override
    public boolean isFriendRequestsEnabled() {
        return this.friendRequestsEnabled;
    }

    @Override
    public void setFriendRequestsEnabled(boolean friendRequestsEnabled) {
        this.friendRequestsEnabled = friendRequestsEnabled;
    }

    @Override
    public HyriPrivateMessagesLevel getPrivateMessagesLevel() {
        return this.privateMessagesLevel;
    }

    @Override
    public void setPrivateMessagesLevel(HyriPrivateMessagesLevel privateMessagesLevel) {
        this.privateMessagesLevel = privateMessagesLevel;
    }

    @Override
    public HyriPlayersVisibilityLevel getPlayersVisibilityLevel() {
        return this.playersVisibilityLevel;
    }

    @Override
    public void setPlayersVisibilityLevel(HyriPlayersVisibilityLevel playersVisibilityLevel) {
        this.playersVisibilityLevel = playersVisibilityLevel;
    }

    @Override
    public boolean isGlobalChatMessagesEnabled() {
        return this.globalChatMessagesEnabled;
    }

    @Override
    public void setGlobalChatMessagesEnabled(boolean globalChatMessagesEnabled) {
        this.globalChatMessagesEnabled = globalChatMessagesEnabled;
    }

    @Override
    public boolean isPrivateMessagesSoundEnabled() {
        return this.privateMessagesSoundEnabled;
    }

    @Override
    public void setPrivateMessagesSoundEnabled(boolean privateMessagesSoundEnabled) {
        this.privateMessagesSoundEnabled = privateMessagesSoundEnabled;
    }

    @Override
    public HyriLanguage getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(HyriLanguage language) {
        this.language = language;
    }

    @Override
    public boolean isAutoQueueEnabled() {
        return this.autoQueueEnabled;
    }

    @Override
    public void setAutoQueueEnabled(boolean autoQueueEnabled) {
        this.autoQueueEnabled = autoQueueEnabled;
    }

    @Override
    public String getChatChannel() {
        return this.chatChannel;
    }

    @Override
    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

}
