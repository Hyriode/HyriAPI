package fr.hyriode.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.HyriRankUpdatedEvent;
import fr.hyriode.api.rank.hyriplus.HyriPlus;
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
     * Get player uuid
     *
     * @return Player {@link UUID}
     */
    UUID getUniqueId();

    /**
     * Check if the player is currently connected on the network
     * Deprecated: see {@link IHyriPlayerManager#isOnline(UUID)}
     *
     * @return <code>true</code> if he is connected
     */
    @Deprecated
    boolean isOnline();

    /**
     * Set if the player is connected or not
     *
     * @param online New online state
     */
    @Deprecated
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
     * Set player's name<br>
     * Warning: Use this method ONLY to change the real player name
     *
     * @param name Player's name
     */
    void setName(String name);

    /**
     * Get the current nickname of the player.<br>
     * Deprecated: see {@link IHyriPlayerSession#getNickname()}
     *
     * @return A {@link IHyriNickname}
     */
    @Deprecated
    IHyriNickname getNickname();

    /**
     * Create a nickname for the player.<br>
     * Deprecated: see {@link IHyriPlayerSession#createNickname(String, String, Skin)}
     *
     * @param name The name to use as a nickname
     * @param skinOwner The owner of the skin that will be used
     * @param skin The skin that will be used
     * @return The created {@link IHyriNickname}
     */
    @Deprecated
    IHyriNickname createNickname(String name, String skinOwner, Skin skin);

    /**
     * Set the current player nickname.<br>
     * Deprecated: see {@link IHyriPlayerSession#setNickname(IHyriNickname)}
     *
     * @param nickname The new {@link IHyriNickname}
     */
    @Deprecated
    void setNickname(IHyriNickname nickname);

    /**
     * Check if the player has a nickname.<br>
     * Deprecated: see {@link IHyriPlayerSession#hasNickname()}
     *
     * @return <code>true</code> if yes
     */
    @Deprecated
    default boolean hasNickname() {
        return this.getNickname() != null;
    }

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
     * Get the {@link UUID} of the last player which this player talks with in private chat.<br>
     * Deprecated: see {@link IHyriPlayerSession#getPrivateMessageTarget()}
     *
     * @return {@link UUID}
     */
    @Deprecated
    UUID getLastPrivateMessagePlayer();

    /**
     * Set the {@link UUID} of the last player which this player talks with in private chat.<br>
     * Deprecated: see {@link IHyriPlayerSession#setPrivateMessageTarget(UUID)} ()}
     *
     * @param player {@link UUID}
     */
    @Deprecated
    void setLastPrivateMessagePlayer(UUID player);

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
     * Get the party {@link UUID} of the player.<br>
     * Deprecated: see {@link IHyriPlayerSession#getParty()}
     *
     * @return Player party
     */
    @Deprecated
    UUID getParty();

    /**
     * Set the party {@link UUID} of the player.<br>
     * Deprecated: see {@link IHyriPlayerSession#setParty(UUID)} ()}
     *
     * @param party Party {@link UUID}
     */
    @Deprecated
    void setParty(UUID party);

    /**
     * Check if the player is in a member of a party.<br>
     * Deprecated: see {@link IHyriPlayerSession#hasParty()}
     *
     * @return <code>true</code> if player has one
     */
    @Deprecated
    boolean hasParty();

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
     * Get the name of the current server where the player is.<br>
     * Warning: if the player is not connected, it will return <code>null</code>.<br>
     * Deprecated: see {@link IHyriPlayerSession#getServer()}
     *
     * @return A server name
     */
    @Deprecated
    String getCurrentServer();

    /**
     * Set the current server where the player is connected.<br>
     * Deprecated: see {@link IHyriPlayerSession#setServer(String)}
     *
     * @param currentServer The name of the server
     */
    @Deprecated
    void setCurrentServer(String currentServer);

    /**
     * Get the last server where the player was.<br>
     * Warning: if the player is connected on a server it will return the old one not the current. To get the current server use {@link #getCurrentServer()}.<br>
     * Deprecated: see {@link IHyriPlayerSession#getLastServer()}
     *
     * @return A server name
     */
    @Deprecated
    String getLastServer();

    /**
     * Set the last server where the player was connected.<br>
     * Deprecated: see {@link IHyriPlayerSession#setLastServer(String)}
     *
     * @param lastServer The name of the server
     */
    @Deprecated
    void setLastServer(String lastServer);

    /**
     * Get the name of the proxy that manages the player.<br>
     * Deprecated: see {@link IHyriPlayerSession#getProxy()}
     *
     * @return A proxy name
     */
    @Deprecated
    String getCurrentProxy();

    /**
     * Set the current proxy that manages the player.<br>
     * Deprecated: see {@link IHyriPlayerSession#setProxy(String)}
     *
     * @param currentProxy A proxy name
     */
    @Deprecated
    void setCurrentProxy(String currentProxy);

    /**
     * Get the handler of player's friends
     *
     * @return A {@link IHyriFriendHandler} instance
     */
    IHyriFriendHandler getFriendHandler();

    /**
     * Get if the player is in moderation mode.<br>
     * Deprecated: see {@link IHyriPlayerSession#isModerating()}
     *
     * @return <code>true</code> if the player is in the moderation mode
     */
    @Deprecated
    boolean isInModerationMode();

    /**
     * Set if the player is in moderation mode.<br>
     * Deprecated: see {@link IHyriPlayerSession#setModerating(boolean)}
     *
     * @param moderationMode <code>true</code> if the player is in the moderation mode
     */
    @Deprecated
    void setInModerationMode(boolean moderationMode);

    /**
     * Get if the player is in vanish mode.<br>
     * Deprecated: see {@link IHyriPlayerSession#isVanished()}
     *
     * @return <code>true</code> if the player is in the vanish mode
     */
    @Deprecated
    boolean isInVanishMode();

    /**
     * Set if the player is in vanish mode.<br>
     * Deprecated: see {@link IHyriPlayerSession#isVanished()}
     *
     * @param vanishMode <code>true</code> if the player is in the vanish mode
     */
    @Deprecated
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