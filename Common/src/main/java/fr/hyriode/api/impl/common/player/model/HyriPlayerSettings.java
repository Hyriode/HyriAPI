package fr.hyriode.api.impl.common.player.model;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.HyriChatChannel;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageUpdatedEvent;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlayerSettings;
import fr.hyriode.api.player.model.SettingsLevel;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayerSettings implements IHyriPlayerSettings, MongoSerializable, DataSerializable {

    @Expose
    private boolean friendRequestsEnabled = true;
    @Expose
    private boolean friendConnectionNotificationEnabled = true;
    @Expose
    private SettingsLevel partyRequestsLevel = SettingsLevel.ALL;
    @Expose
    private SettingsLevel privateMessagesLevel = SettingsLevel.ALL;
    @Expose
    private SettingsLevel privateMessagesSoundLevel = SettingsLevel.ALL;
    @Expose
    private SettingsLevel playersVisibilityLevel = SettingsLevel.ALL;
    @Expose
    private SettingsLevel globalChatLevel = SettingsLevel.ALL;
    @Expose
    private HyriLanguage language = HyriLanguage.FR;
    @Expose
    private boolean autoQueueEnabled = true;
    @Expose
    private boolean followPartyEnabled = true;
    @Expose
    private HyriChatChannel chatChannel = HyriChatChannel.GLOBAL;

    private final IHyriPlayer player;

    public HyriPlayerSettings(IHyriPlayer player) {
        this.player = player;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("friendsRequests", this.friendRequestsEnabled);
        document.append("friendConnectionNotification", this.friendConnectionNotificationEnabled);
        document.append("partyRequests", this.partyRequestsLevel.name());
        document.append("privateMessages", this.privateMessagesLevel.name());
        document.append("privateMessagesSound", this.privateMessagesSoundLevel.name());
        document.append("playersVisibility", this.playersVisibilityLevel.name());
        document.append("globalChat", this.globalChatLevel.name());
        document.append("language", this.language.name());
        document.append("autoQueue", this.autoQueueEnabled);
        document.append("followParty", this.followPartyEnabled);
        document.append("chatChannel", this.chatChannel.name());
    }

    @Override
    public void load(MongoDocument document) {
        this.friendRequestsEnabled = document.getBoolean("friendsRequests");
        this.friendConnectionNotificationEnabled = document.getBoolean("friendConnectionNotification");
        this.partyRequestsLevel = SettingsLevel.valueOf(document.getString("partyRequests"));
        this.privateMessagesLevel = SettingsLevel.valueOf(document.getString("privateMessages"));
        this.privateMessagesSoundLevel = SettingsLevel.valueOf(document.getString("privateMessagesSound"));
        this.playersVisibilityLevel = SettingsLevel.valueOf(document.getString("playersVisibility"));
        this.globalChatLevel = SettingsLevel.valueOf(document.getString("globalChat"));
        this.language = HyriLanguage.valueOf(document.getString("language"));
        this.autoQueueEnabled = document.getBoolean("autoQueue");
        this.followPartyEnabled = document.getBoolean("followParty");
        this.chatChannel = HyriChatChannel.valueOf(document.getString("chatChannel"));
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeBoolean(this.friendRequestsEnabled);
        output.writeBoolean(this.friendConnectionNotificationEnabled);
        output.writeString(this.partyRequestsLevel.name());
        output.writeString(this.privateMessagesLevel.name());
        output.writeString(this.privateMessagesSoundLevel.name());
        output.writeString(this.playersVisibilityLevel.name());
        output.writeString(this.globalChatLevel.name());
        output.writeString(this.language.name());
        output.writeBoolean(this.autoQueueEnabled);
        output.writeBoolean(this.followPartyEnabled);
        output.writeString(this.chatChannel.name());
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.friendRequestsEnabled = input.readBoolean();
        this.friendConnectionNotificationEnabled = input.readBoolean();
        this.partyRequestsLevel = SettingsLevel.valueOf(input.readString());
        this.privateMessagesLevel = SettingsLevel.valueOf(input.readString());
        this.privateMessagesSoundLevel = SettingsLevel.valueOf(input.readString());
        this.playersVisibilityLevel = SettingsLevel.valueOf(input.readString());
        this.globalChatLevel = SettingsLevel.valueOf(input.readString());
        this.language = HyriLanguage.valueOf(input.readString());
        this.autoQueueEnabled = input.readBoolean();
        this.followPartyEnabled = input.readBoolean();
        this.chatChannel = HyriChatChannel.valueOf(input.readString());
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

        HyriAPI.get().getEventBus().publish(new HyriLanguageUpdatedEvent(this.player.getUniqueId(), this.language));
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
