package fr.hyriode.api.impl.common.settings;

import com.google.gson.Gson;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.HyriChatChannel;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageUpdatedEvent;
import fr.hyriode.api.settings.SettingsLevel;
import fr.hyriode.api.settings.IHyriPlayerSettings;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayerSettings implements IHyriPlayerSettings {

    private boolean friendRequestsEnabled = true;
    private boolean friendConnectionNotificationEnabled = true;
    private SettingsLevel partyRequestsLevel = SettingsLevel.ALL;
    private SettingsLevel privateMessagesLevel = SettingsLevel.ALL;
    private SettingsLevel privateMessagesSoundLevel = SettingsLevel.ALL;
    private SettingsLevel playersVisibilityLevel = SettingsLevel.ALL;
    private SettingsLevel globalChatLevel = SettingsLevel.ALL;
    private HyriLanguage language = HyriLanguage.FR;
    private boolean autoQueueEnabled = true;
    private boolean followPartyEnabled = true;
    private HyriChatChannel chatChannel = HyriChatChannel.GLOBAL;

    private transient UUID playerId;

    public void providePlayerId(UUID playerId) {
        this.playerId = playerId;
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
    public boolean isFriendConnectionNotificationEnabled() {
        return this.friendConnectionNotificationEnabled;
    }

    @Override
    public void setFriendConnectionNotificationEnabled(boolean friendConnectionNotificationEnabled) {
        this.friendConnectionNotificationEnabled = friendConnectionNotificationEnabled;
    }

    @Override
    public SettingsLevel getPartyRequestsLevel() {
        return this.partyRequestsLevel;
    }

    @Override
    public void setPartyRequestsLevel(SettingsLevel partyRequestsLevel) {
        if (partyRequestsLevel == SettingsLevel.PARTY) {
            throw new IllegalArgumentException();
        }

        this.partyRequestsLevel = partyRequestsLevel;
    }

    @Override
    public SettingsLevel getPrivateMessagesLevel() {
        return this.privateMessagesLevel;
    }

    @Override
    public void setPrivateMessagesLevel(SettingsLevel privateMessagesLevel) {
        if (privateMessagesLevel == SettingsLevel.PARTY) {
            throw new IllegalArgumentException();
        }

        this.privateMessagesLevel = privateMessagesLevel;
    }

    @Override
    public SettingsLevel getPrivateMessagesSoundLevel() {
        return this.privateMessagesSoundLevel;
    }

    @Override
    public void setPrivateMessagesSoundLevel(SettingsLevel privateMessagesSoundLevel) {
        if (privateMessagesSoundLevel == SettingsLevel.PARTY) {
            throw new IllegalArgumentException();
        }

        this.privateMessagesSoundLevel = privateMessagesSoundLevel;
    }

    @Override
    public SettingsLevel getPlayersVisibilityLevel() {
        return this.playersVisibilityLevel;
    }

    @Override
    public void setPlayersVisibilityLevel(SettingsLevel playersVisibilityLevel) {
        this.playersVisibilityLevel = playersVisibilityLevel;
    }

    @Override
    public SettingsLevel getGlobalChatLevel() {
        return this.globalChatLevel;
    }

    @Override
    public void setGlobalChatLevel(SettingsLevel globalChatLevel) {
        if (globalChatLevel == SettingsLevel.PARTY) {
            throw new IllegalArgumentException();
        }

        this.globalChatLevel = globalChatLevel;
    }

    @Override
    public HyriLanguage getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(HyriLanguage language) {
        this.language = language;

        HyriAPI.get().getEventBus().publish(new HyriLanguageUpdatedEvent(this.playerId, this.language));
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
    public boolean isFollowPartyEnabled() {
        return this.followPartyEnabled;
    }

    @Override
    public void setFollowPartyEnabled(boolean followPartyEnabled) {
        this.followPartyEnabled = followPartyEnabled;
    }

    @Override
    public HyriChatChannel getChatChannel() {
        return this.chatChannel;
    }

    @Override
    public void setChatChannel(HyriChatChannel chatChannel) {
        this.chatChannel = chatChannel;
    }

}
