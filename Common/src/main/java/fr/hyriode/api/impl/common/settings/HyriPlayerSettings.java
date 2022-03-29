package fr.hyriode.api.impl.common.settings;

import fr.hyriode.api.settings.*;

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
    private boolean tagSoundEnabled;
    private HyriLanguage language;
    private String chatChannel;

    public HyriPlayerSettings(boolean partyRequestsEnabled, boolean friendRequestsEnabled, HyriPrivateMessagesLevel privateMessagesLevel, HyriPlayersVisibilityLevel playersVisibilityLevel, boolean globalChatMessagesEnabled, boolean privateMessagesSoundEnabled, boolean tagSoundEnabled, HyriLanguage language, String chatChannel) {
        this.partyRequestsEnabled = partyRequestsEnabled;
        this.friendRequestsEnabled = friendRequestsEnabled;
        this.privateMessagesLevel = privateMessagesLevel;
        this.playersVisibilityLevel = playersVisibilityLevel;
        this.globalChatMessagesEnabled = globalChatMessagesEnabled;
        this.privateMessagesSoundEnabled = privateMessagesSoundEnabled;
        this.tagSoundEnabled = tagSoundEnabled;
        this.language = language;
        this.chatChannel = chatChannel;
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
    public boolean isTagSoundEnabled() {
        return this.tagSoundEnabled;
    }

    @Override
    public void setTagSoundEnabled(boolean tagSoundEnabled) {
        this.tagSoundEnabled = tagSoundEnabled;
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
    public String getChatChannel() {
        return this.chatChannel;
    }

    @Override
    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

}
