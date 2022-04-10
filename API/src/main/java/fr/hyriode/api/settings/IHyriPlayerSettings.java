package fr.hyriode.api.settings;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 10:33
 */
public interface IHyriPlayerSettings {

    /**
     * Get if party requests are enabled
     *
     * @return - <code>true</code> if enabled
     */
    boolean isPartyRequestsEnabled();

    /**
     * Set party requests enabled
     *
     * @param partyRequestsEnabled - <code>true</code> if enabled
     */
    void setPartyRequestsEnabled(boolean partyRequestsEnabled);

    /**
     * Get if friend requests are enabled
     *
     * @return - <code>true</code> if enabled
     */
    boolean isFriendRequestsEnabled();

    /**
     * Set friend requests enabled
     *
     * @param friendRequestsEnabled - <code>true</code> if enabled
     */
    void setFriendRequestsEnabled(boolean friendRequestsEnabled);

    /**
     * Get private messages level
     *
     * @return - {@link HyriPrivateMessagesLevel}
     */
    HyriPrivateMessagesLevel getPrivateMessagesLevel();

    /**
     * Set private messages level
     *
     * @param privateMessagesLevel - {@link HyriPrivateMessagesLevel}
     */
    void setPrivateMessagesLevel(HyriPrivateMessagesLevel privateMessagesLevel);

    /**
     * Get players visibility level
     *
     * @return - {@link HyriPlayersVisibilityLevel}
     */
    HyriPlayersVisibilityLevel getPlayersVisibilityLevel();

    /**
     * Set players visibility level
     *
     * @param playersVisibilityLevel - {@link HyriPlayersVisibilityLevel}
     */
    void setPlayersVisibilityLevel(HyriPlayersVisibilityLevel playersVisibilityLevel);

    /**
     * Get if messages in global chat are enabled
     *
     * @return - <code>true</code> if enabled
     */
    boolean isGlobalChatMessagesEnabled();

    /**
     * Set if messages in global chat are enabled
     *
     * @param globalChatMessagesEnabled - <code>true</code> if enabled
     */
    void setGlobalChatMessagesEnabled(boolean globalChatMessagesEnabled);

    /**
     * Get if private messages sound is enabled
     *
     * @return - <code>true</code> if enabled
     */
    boolean isPrivateMessagesSoundEnabled();

    /**
     * Set if private messages sound is enabled
     *
     * @param privateMessagesSoundEnabled - <code>true</code> if enabled
     */
    void setPrivateMessagesSoundEnabled(boolean privateMessagesSoundEnabled);

    /**
     * Get if tag sound is enabled
     *
     * @return - <code>true</code> if enabled
     */
    boolean isTagSoundEnabled();

    /**
     * Set if tag sound is enabled
     *
     * @param tagSoundEnabled - <code>true</code> if enabled
     */
    void setTagSoundEnabled(boolean tagSoundEnabled);

    /**
     * Get player language
     *
     * @return - {@link HyriLanguage}
     */
    HyriLanguage getLanguage();


    /**
     * Set player language
     *
     * @param language - {@link HyriLanguage}
     */
    void setLanguage(HyriLanguage language);

    /**
     * Get the player chat channel
     *
     * @return - The player chat channel
     */
    String getChatChannel();

    /**
     * Set the player chat channel
     *
     * @param chatChannel - String
     */
    void setChatChannel(String chatChannel);

}
