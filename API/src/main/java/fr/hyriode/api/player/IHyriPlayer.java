package fr.hyriode.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.HyriRankUpdatedEvent;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.api.transaction.IHyriTransaction;
import fr.hyriode.api.transaction.IHyriTransactionContent;
import fr.hyriode.api.util.Skin;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
     * Get the prefix of the player
     *
     * @return A prefix
     */
    String getPrefix();

    /**
     * Get default player name
     *
     * @return Default name
     */
    String getName();

    /**
     * Get the display name of the player.<br>
     * By default, it will be its name but if he has a nickname it will return his nickname
     *
     * @return A name
     */
    String getDisplayName();

    /**
     * Set player's name<br>
     * Warning: Use this method ONLY to change the real player name
     *
     * @param name Player's name
     */
    void setName(String name);

    /**
     * Get the current nickname of the player.<br>
     * It can be null if the player is not nicked
     *
     * @return A {@link IHyriNickname}
     */
    IHyriNickname getNickname();

    /**
     * Create a nickname for the player
     *
     * @param name The name to use as a nickname
     * @param skinOwner The owner of the skin that will be used
     * @param skin The skin that will be usezd
     * @return The created {@link IHyriNickname}
     */
    IHyriNickname createNickname(String name, String skinOwner, Skin skin);

    /**
     * Set the current player nickname
     *
     * @param nickname The new {@link IHyriNickname}
     */
    void setNickname(IHyriNickname nickname);

    /**
     * Check if the player has a nickname
     *
     * @return <code>true</code> if yes
     */
    default boolean hasNickname() {
        return this.getNickname() != null;
    }

    /**
     * Get the player name with the rank prefix
     *
     * @param nickname <code>true</code> if the nickname is taken in account
     * @return Player names with the rank prefix
     */
    String getNameWithRank(boolean nickname);

    /**
     * Get the player name with the rank prefix
     *
     * @return Player names with the rank prefix
     */
    default String getNameWithRank() {
        return this.getNameWithRank(false);
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

        HyriAPI.get().getEventBus().register(new HyriRankUpdatedEvent(this.getUniqueId()));
    }

    /**
     * Set the staff rank of the player
     *
     * @param staffRankType A {@link HyriStaffRankType}
     */
    default void setStaffRank(HyriStaffRankType staffRankType) {
        this.getRank().setStaffType(staffRankType);

        HyriAPI.get().getEventBus().register(new HyriRankUpdatedEvent(this.getUniqueId()));
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
     * Get the priority of the rank in the tab list
     *
     * @return A number that represents a priority
     */
    int getTabListPriority();

    /**
     * Add a transaction to the player account
     *
     * @param type The type of transaction to add
     * @param name The name of the transaction to add
     * @param content The content of the transaction
     * @return <code>true</code> if the transaction has been added and doesn't already exist
     */
    boolean addTransaction(String type, String name, IHyriTransactionContent content);

    /**
     * Remove a transaction by giving its type and name
     *
     * @param type The type of the transaction to remove
     * @param name The name of the transaction to remove
     * @return <code>true</code> if the transaction existed and has been removed
     */
    boolean removeTransaction(String type, String name);

    /**
     * Get a transaction by giving its type and name
     *
     * @param type The type of the transaction to get
     * @param name The name of the transaction to get
     * @return The {@linkplain  IHyriTransaction found transaction}; or <code>null</code> if the transaction doesn't exist
     */
    IHyriTransaction getTransaction(String type, String name);

    /**
     * Check if the player has done a transaction
     *
     * @param type The type of the transaction to check
     * @param name The name of the transaction to check
     * @return <code>true</code> if the player has done the transaction
     */
    boolean hasTransaction(String type, String name);

    /**
     * Get all the transactions of a given type
     *
     * @param type The type of the transactions to get
     * @return A list of {@linkplain IHyriTransaction transaction}
     */
    List<? extends IHyriTransaction> getTransactions(String type);

    /**
     * Get all the transactions the player has done
     *
     * @return A map linking lists of {@linkplain IHyriTransaction transactions} to their type
     */
    Map<String, ? extends List<? extends IHyriTransaction>> getTransactions();

    /**
     * Get all the existing transactions types
     *
     * @return A list of type
     */
    List<String> getTransactionsTypes();

    /**
     * Update the player account in database
     */
    default void update() {
        HyriAPI.get().getPlayerManager().sendPlayer(this);
    }

    /**
     * Get the account of a given player
     *
     * @param playerId The unique id of the player
     * @return A {@link IHyriPlayer} object
     */
    static IHyriPlayer get(UUID playerId) {
        return HyriAPI.get().getPlayerManager().getPlayer(playerId);
    }

}