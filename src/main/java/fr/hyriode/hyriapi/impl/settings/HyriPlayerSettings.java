package fr.hyriode.hyriapi.impl.settings;

import fr.hyriode.hyriapi.settings.HyriLanguage;
import fr.hyriode.hyriapi.settings.HyriPlayersVisibilityLevel;
import fr.hyriode.hyriapi.settings.HyriPrivateMessagesLevel;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;

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

    public HyriPlayerSettings(boolean partyRequestsEnabled, boolean friendRequestsEnabled, HyriPrivateMessagesLevel privateMessagesLevel, HyriPlayersVisibilityLevel playersVisibilityLevel, boolean globalChatMessagesEnabled, boolean privateMessagesSoundEnabled, boolean tagSoundEnabled, HyriLanguage language) {
        this.partyRequestsEnabled = partyRequestsEnabled;
        this.friendRequestsEnabled = friendRequestsEnabled;
        this.privateMessagesLevel = privateMessagesLevel;
        this.playersVisibilityLevel = playersVisibilityLevel;
        this.globalChatMessagesEnabled = globalChatMessagesEnabled;
        this.privateMessagesSoundEnabled = privateMessagesSoundEnabled;
        this.tagSoundEnabled = tagSoundEnabled;
        this.language = language;
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

}
