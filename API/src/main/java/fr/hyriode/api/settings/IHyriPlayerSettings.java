package fr.hyriode.api.settings;

import fr.hyriode.api.language.HyriLanguage;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 10:33
 */
public interface IHyriPlayerSettings {

    /**
     * Get if friend requests are enabled
     *
     * @return <code>true</code> if enabled
     */
    boolean isFriendRequestsEnabled();

    /**
     * Set friend requests enabled
     *
     * @param value <code>true</code> if enabled
     */
    void setFriendRequestsEnabled(boolean value);

    /**
     * Check if notifications when a friend logged-in is enabled
     *
     * @return <code>true</code> if yes
     */
    boolean isFriendConnectionNotificationEnabled();

    /**
     * Set if notifications when a friend logged-in is enabled
     *
     * @param value <code>true</code> if enabled
     */
    void setFriendConnectionNotificationEnabled(boolean value);

    /**
     * Get the party requests level
     *
     * @return A {@linkplain HyriSettingsLevel level}
     */
    HyriSettingsLevel getPartyRequestsLevel();

    /**
     * Set the party requests level
     *
     * @param level A {@linkplain HyriSettingsLevel level}
     */
    void setPartyRequestsLevel(HyriSettingsLevel level);

    /**
     * Get private messages level
     *
     * @return {@link HyriSettingsLevel}
     */
    HyriSettingsLevel getPrivateMessagesLevel();

    /**
     * Set private messages level
     *
     * @param level {@link HyriSettingsLevel}
     */
    void setPrivateMessagesLevel(HyriSettingsLevel level);

    /**
     * Get the private messages sound level
     *
     * @return A {@linkplain HyriSettingsLevel level}
     */
    HyriSettingsLevel getPrivateMessagesSoundLevel();

    /**
     * Set the private messages sound level
     *
     * @param level A {@linkplain HyriSettingsLevel level}
     */
    void setPrivateMessagesSoundLevel(HyriSettingsLevel level);

    /**
     * Get players visibility level
     *
     * @return {@link HyriSettingsLevel}
     */
    HyriSettingsLevel getPlayersVisibilityLevel();

    /**
     * Set players visibility level
     *
     * @param level {@link HyriSettingsLevel}
     */
    void setPlayersVisibilityLevel(HyriSettingsLevel level);

    /**
     * Get the global chat messages visibility level
     *
     * @return A {@linkplain HyriSettingsLevel level}
     */
    HyriSettingsLevel getGlobalChatLevel();

    /**
     * Set the global chat messages visibility level
     *
     * @param level A {@linkplain HyriSettingsLevel level}
     */
    void setGlobalChatLevel(HyriSettingsLevel level);

    /**
     * Get player language
     *
     * @return {@link HyriLanguage}
     */
    HyriLanguage getLanguage();

    /**
     * Set player language
     *
     * @param language {@link HyriLanguage}
     */
    void setLanguage(HyriLanguage language);

    /**
     * Check if the player will be automatically added in a new queue after a game
     *
     * @return <code>true</code> if yes
     */
    boolean isAutoQueueEnabled();

    /**
     * Set if the player will be automatically added in a new queue after a game
     *
     * @param autoQueueEnabled New value
     */
    void setAutoQueueEnabled(boolean autoQueueEnabled);

    /**
     * Get the player chat channel
     *
     * @return The player chat channel
     */
    String getChatChannel();

    /**
     * Set the player chat channel
     *
     * @param chatChannel String
     */
    void setChatChannel(String chatChannel);

}
