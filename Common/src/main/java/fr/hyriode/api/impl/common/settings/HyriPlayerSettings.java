package fr.hyriode.api.impl.common.settings;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.HyriChatChannel;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageUpdatedEvent;
import fr.hyriode.api.settings.HyriSettingsLevel;
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
    private HyriSettingsLevel partyRequestsLevel = HyriSettingsLevel.ALL;
    private HyriSettingsLevel privateMessagesLevel = HyriSettingsLevel.ALL;
    private HyriSettingsLevel privateMessagesSoundLevel = HyriSettingsLevel.ALL;
    private HyriSettingsLevel playersVisibilityLevel = HyriSettingsLevel.ALL;
    private HyriSettingsLevel globalChatLevel = HyriSettingsLevel.ALL;
    private HyriLanguage language = HyriLanguage.FR;
    private boolean autoQueueEnabled = true;
    private String chatChannel = HyriChatChannel.GLOBAL.getChannel();

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
    public HyriSettingsLevel getPartyRequestsLevel() {
        return this.partyRequestsLevel;
    }

    @Override
    public void setPartyRequestsLevel(HyriSettingsLevel partyRequestsLevel) {
        if (partyRequestsLevel == HyriSettingsLevel.PARTY) {
            throw new IllegalArgumentException();
        }

        this.partyRequestsLevel = partyRequestsLevel;
    }

    @Override
    public HyriSettingsLevel getPrivateMessagesLevel() {
        return this.privateMessagesLevel;
    }

    @Override
    public void setPrivateMessagesLevel(HyriSettingsLevel privateMessagesLevel) {
        if (privateMessagesLevel == HyriSettingsLevel.PARTY) {
            throw new IllegalArgumentException();
        }

        this.privateMessagesLevel = privateMessagesLevel;
    }

    @Override
    public HyriSettingsLevel getPrivateMessagesSoundLevel() {
        return this.privateMessagesSoundLevel;
    }

    @Override
    public void setPrivateMessagesSoundLevel(HyriSettingsLevel privateMessagesSoundLevel) {
        if (privateMessagesSoundLevel == HyriSettingsLevel.PARTY) {
            throw new IllegalArgumentException();
        }

        this.privateMessagesSoundLevel = privateMessagesSoundLevel;
    }

    @Override
    public HyriSettingsLevel getPlayersVisibilityLevel() {
        return this.playersVisibilityLevel;
    }

    @Override
    public void setPlayersVisibilityLevel(HyriSettingsLevel playersVisibilityLevel) {
        this.playersVisibilityLevel = playersVisibilityLevel;
    }

    @Override
    public HyriSettingsLevel getGlobalChatLevel() {
        return this.globalChatLevel;
    }

    @Override
    public void setGlobalChatLevel(HyriSettingsLevel globalChatLevel) {
        if (globalChatLevel == HyriSettingsLevel.PARTY) {
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
    public String getChatChannel() {
        return this.chatChannel;
    }

    @Override
    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

}
