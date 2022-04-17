package fr.hyriode.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.settings.IHyriPlayerSettings;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 18:40
 */
public interface IHyriPlayer {

    /**
     * Check if the player is currently connected on the network
     *
     * @return <code>true</code> if he is connected
     */
    boolean isOnline();

    /**
     * Set if the player is connected or not
     *
     * @param online New online state
     */
    void setOnline(boolean online);

    /**
     * Get default player name
     *
     * @return Default name
     */
    String getName();

    /**
     * Set player's name<br>
     * Warning: Use this method ONLY to change the real player name
     *
     * @param name Player's name
     */
    void setName(String name);

    /**
     * Get the custom name of player
     *
     * @return Custom name
     */
    String getCustomName();

    /**
     * Set player's custom name
     *
     * @param customName Player's custom name
     */
    void setCustomName(String customName);

    /**
     * Get current display name: name or custom name
     *
     * @return Display name
     */
    String getDisplayName();

    /**
     * Get the player name with the rank prefix
     *
     * @return Player names with the rank prefix
     */
    String getNameWithRank();

    /**
     * Set the player name with the rank prefix
     *
     * @param nameWithRank Player names with the rank prefix
     */
    void setNameWithRank(String nameWithRank);

    /**
     * Get if player has a custom name
     *
     * @return <code>true</code> if player has a custom name
     */
    default boolean hasCustomName() {
        return this.getCustomName() != null;
    }

    /**
     * Get player uuid
     *
     * @return Player {@link UUID}
     */
    UUID getUniqueId();

    /**
     * Get the first login {@link Date} of the player
     *
     * @return {@link Date}
     */
    Date getFirstLoginDate();

    /**
     * Get the last login {@link Date} of the player
     *
     * @return {@link Date}
     */
    Date getLastLoginDate();

    /**
     * Set the last login {@link Date} of the player
     *
     * @param date The new {@link Date}
     */
    void setLastLoginDate(Date date);

    /**
     * Get player play time on the network
     *
     * @return Player play time
     */
    long getPlayTime();

    /**
     * Set player play time on the network
     *
     * @param time New player play time
     */
    void setPlayTime(long time);

    /**
     * Get player rank
     *
     * @return {@link HyriRank}
     */
    HyriRank getRank();

    /**
     * Set player rank
     *
     * @param rank {@link HyriRank}
     */
    void setRank(HyriRank rank);

    /**
     * Set the player rank of the player
     *
     * @param playerRankType A {@link HyriPlayerRankType}
     */
    default void setPlayerRank(HyriPlayerRankType playerRankType) {
        this.getRank().setPlayerType(playerRankType);
    }

    /**
     * Set the staff rank of the player
     *
     * @param staffRankType A {@link HyriStaffRankType}
     */
    default void setStaffRank(HyriStaffRankType staffRankType) {
        this.getRank().setStaffType(staffRankType);
    }

    /**
     * Get Hyri+ offer
     *
     * @return The {@link HyriPlus} offer instance
     */
    HyriPlus getHyriPlus();

    /**
     * Set Hyri+ offer
     *
     * @param hyriPlus New {@link HyriPlus} offer instance
     */
    void setHyriPlus(HyriPlus hyriPlus);

    /**
     * Check if the player has Hyri+ offer
     *
     * @return <code>true</code> if the player has Hyri+
     */
    boolean hasHyriPlus();

    /**
     * Get the {@link UUID} of the last player which this player talks with in private chat
     *
     * @return {@link UUID}
     */
    UUID getLastPrivateMessagePlayer();

    /**
     * Set the {@link UUID} of the last player which this player talks with in private chat
     *
     * @param player {@link UUID}
     */
    void setLastPrivateMessagePlayer(UUID player);

    /**
     * Get player Hyris money
     *
     * @return Hyris money
     */
    IHyriMoney getHyris();

    /**
     * Get the party {@link UUID} of the player
     *
     * @return Player party
     */
    UUID getParty();

    /**
     * Set the party {@link UUID} of the player
     *
     * @param party Party {@link UUID}
     */
    void setParty(UUID party);

    /**
     * Check if the player is in a member of a party
     *
     * @return <code>true</code> if player has one
     */
    boolean hasParty();

    /**
     * Get the settings of the player
     *
     * @return Player settings
     */
    IHyriPlayerSettings getSettings();

    /**
     * Set the settings of the player
     *
     * @param settings New settings
     */
    void setSettings(IHyriPlayerSettings settings);

    /**
     * Get the name of the current server where the player is.<br>
     * Warning: if the player is not connected, it will return <code>null</code>
     *
     * @return A server name
     */
    String getCurrentServer();

    /**
     * Set the current server where the player is connected
     *
     * @param currentServer The name of the server
     */
    void setCurrentServer(String currentServer);

    /**
     * Get the last server where the player was.<br>
     * Warning: if the player is connected on a server it will return the old one not the current. To get the current server use {@link #getCurrentServer()}
     *
     * @return A server name
     */
    String getLastServer();

    /**
     * Set the last server where the player was connected
     *
     * @param lastServer The name of the server
     */
    void setLastServer(String lastServer);

    /**
     * Get the name of the proxy that manages the player
     *
     * @return A proxy name
     */
    String getCurrentProxy();

    /**
     * Set the current proxy that manages the player
     *
     * @param currentProxy A proxy name
     */
    void setCurrentProxy(String currentProxy);

    /**
     * Get the handler of player's friends
     *
     * @return A {@link IHyriFriendHandler} instance
     */
    IHyriFriendHandler getFriendHandler();

    /**
     * Get if the player is in moderation mode
     *
     * @return <code>true</code> if the player is in the moderation mode
     */
    boolean isInModerationMode();

    /**
     * Set if the player is in moderation mode
     *
     * @param moderationMode <code>true</code> if the player is in the moderation mode
     */
    void setInModerationMode(boolean moderationMode);

    /**
     * Get if the player is in vanish mode
     *
     * @return <code>true</code> if the player is in the vanish mode
     */
    boolean isInVanishMode();

    /**
     * Set if the player is in vanish mode
     *
     * @param vanishMode <code>true</code> if the player is in the vanish mode
     */
    void setInVanishMode(boolean vanishMode);

    /**
     * Get all the statistics key linked to the player account
     *
     * @return A list of key
     */
    List<String> getStatistics();

    /**
     * Get statistics from its key
     *
     * @param key The key of the statistics
     * @param statisticsClass The class used to deserialize statistics
     * @param <T> The type of {@link HyriPlayerData} to return
     * @return A {@link HyriPlayerData} object
     */
    <T extends HyriPlayerData> T getStatistics(String key, Class<T> statisticsClass);

    /**
     * Add a statistics in player account
     *
     * @param key The key of the statistics
     * @param statistics The statistics to add
     */
    void addStatistics(String key, HyriPlayerData statistics);

    /**
     * Remove a statistics from player account
     *
     * @param key The key of the statistics to get
     */
    void removeStatistics(String key);

    /**
     * Check if the player has a statistics
     *
     * @param key The key of the statistics
     * @return <code>true</code> if the player has the statistics
     */
    boolean hasStatistics(String key);

    /**
     * Get all the data key linked to the player account
     *
     * @return A list of key
     */
    List<String> getData();

    /**
     * Get data from its key
     *
     * @param key The key of the data
     * @param dataClass The class used to deserialize data
     * @param <T> The type of {@link HyriPlayerData} to return
     * @return A {@link HyriPlayerData} object
     */
    <T extends HyriPlayerData> T getData(String key, Class<T> dataClass);

    /**
     * Add a data in player account
     *
     * @param key The key of the data
     * @param data The data to add
     */
    void addData(String key, HyriPlayerData data);

    /**
     * Remove a data from player account
     *
     * @param key The key of the data to get
     */
    void removeData(String key);

    /**
     * Check if the player has a data
     *
     * @param key The key of the data
     * @return <code>true</code> if the player has the data
     */
    boolean hasData(String key);

    /**
     * Get the player leveling on the network
     *
     * @return A {@link IHyriLeveling} instance
     */
    IHyriLeveling getNetworkLeveling();

    /**
     * Get the priority of the player in queues
     *
     * @return A number that represents a priority
     */
    int getPriority();

    /**
     * Update the player account in database
     */
    default void update() {
        HyriAPI.get().getPlayerManager().sendPlayer(this);
    }

}