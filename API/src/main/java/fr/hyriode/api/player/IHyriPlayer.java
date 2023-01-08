package fr.hyriode.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.HyriRankUpdatedEvent;
import fr.hyriode.api.rank.hyriplus.HyriPlus;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.api.transaction.IHyriTransaction;
import fr.hyriode.api.transaction.IHyriTransactionContent;

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
     * Get player uuid
     *
     * @return Player {@link UUID}
     */
    UUID getUniqueId();

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
     * Set player's name<br>
     * Warning: Use this method ONLY to change the real player name
     *
     * @param name Player's name
     */
    void setName(String name);

    /**
     * Get the player name with the rank prefix
     *
     * @return Player names with the rank prefix
     */
    String getNameWithRank();

    /**
     * Check whether the player is premium or not
     *
     * @return <code>true</code> if the player is premium
     */
    boolean isPremium();

    /**
     * Get the first login date of the player
     *
     * @return A timestamp (in milliseconds)
     */
    long getFirstLoginDate();

    /**
     * Get the last login date of the player
     *
     * @return A timestamp (in milliseconds)
     */
    long getLastLoginDate();

    /**
     * Set the last login date of the player
     *
     * @param date The new date (in milliseconds)
     */
    void setLastLoginDate(long date);

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

        HyriAPI.get().getEventBus().publish(new HyriRankUpdatedEvent(this.getUniqueId()));
    }

    /**
     * Set the staff rank of the player
     *
     * @param staffRankType A {@link HyriStaffRankType}
     */
    default void setStaffRank(HyriStaffRankType staffRankType) {
        this.getRank().setStaffType(staffRankType);

        HyriAPI.get().getEventBus().publish(new HyriRankUpdatedEvent(this.getUniqueId()));
    }

    /**
     * Get Hyri+ offer
     *
     * @return The {@link HyriPlus} offer instance
     */
    HyriPlus getHyriPlus();

    /**
     * Check if the player has Hyri+ offer
     *
     * @return <code>true</code> if the player has Hyri+
     */
    boolean hasHyriPlus();

    /**
     * Get the available hosts of the player
     *
     * @return A number between 0 and {@link Integer#MAX_VALUE}
     */
    int getAvailableHosts();

    /**
     * Set the available hosts of the player
     *
     * @param availableHosts A number between 0 and {@link Integer#MAX_VALUE}
     */
    void setAvailableHosts(int availableHosts);

    /**
     * Get all the players banned from player host
     *
     * @return A list of player {@link UUID}
     */
    List<UUID> getPlayersBannedFromHost();

    /**
     * Add a player banned from player host
     *
     * @param playerId The {@link UUID} of the player
     */
    void addPlayerBannedFromHost(UUID playerId);

    /**
     * Remove a player banned from player host
     *
     * @param playerId The {@link UUID} of the player
     */
    void removePlayerBannedFromHost(UUID playerId);

    /**
     * Get the favorite host configs of the player
     *
     * @return A list of config id
     */
    List<String> getFavoriteHostConfigs();

    /**
     * Add a favorite host config to the player account
     *
     * @param configId The identifier of the config to add
     */
    void addFavoriteHostConfig(String configId);

    /**
     * Remove a favorite host config from the player account
     *
     * @param configId The identifier of the config to remove
     */
    void removeFavoriteHostConfig(String configId);

    /**
     * Get player Hyris money
     *
     * @return A {@link IHyriMoney} instance
     */
    IHyriMoney getHyris();

    /**
     * Get player Gems money
     *
     * @return A {@link IHyriMoney} instance
     */
    IHyriMoney getGems();

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
     * Get the handler of player's friends
     *
     * @return A {@link IHyriFriendHandler} instance
     */
    IHyriFriendHandler getFriendHandler();

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
     * Add a transaction to the player account
     *
     * @param type The type of transaction to add
     * @param name The name of the transaction to add
     * @param content The content of the transaction
     * @return <code>true</code> if the transaction has been added and doesn't already exist
     */
    boolean addTransaction(String type, String name, IHyriTransactionContent content);

    /**
     * Add a transaction to the player account but with an auto-generated name
     *
     * @param type The type of transaction to add
     * @param content The content of the transaction
     * @return <code>true</code> if the transaction has been added and doesn't already exist
     */
    default boolean addTransaction(String type, IHyriTransactionContent content) {
        return this.addTransaction(type, UUID.randomUUID().toString().split("-")[0], content);
    }

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
     * Update the player account in cache
     */
    default void update() {
        HyriAPI.get().getPlayerManager().updatePlayer(this);
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