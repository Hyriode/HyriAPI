package fr.hyriode.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.leveling.NetworkLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.model.IHyriPlayerSettings;
import fr.hyriode.api.player.model.IHyriPlus;
import fr.hyriode.api.player.model.modules.*;
import fr.hyriode.api.rank.IHyriRank;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
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
     * Get the history of the player's names
     *
     * @return A list of name
     */
    Set<String> getNameHistory();

    /**
     * Get the prefix of the player
     *
     * @return A prefix
     */
    String getPrefix();

    /**
     * Get the player name with the rank prefix
     *
     * @return Player names with the rank prefix
     */
    String getNameWithRank();

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
     * Get the last IP address of the player
     *
     * @return An IP address
     */
    @NotNull String getLastIP();

    /**
     * Set the last IP address of the player
     *
     * @param ip A new IP address
     */
    void setLastIP(@NotNull String ip);

    /**
     * Get the rank of the player
     *
     * @return The {@linkplain IHyriRank rank} object
     */
    @NotNull IHyriRank getRank();

    /**
     * Get the Hyri+ offer
     *
     * @return The {@link IHyriPlus} offer instance
     */
    IHyriPlus getHyriPlus();

    /**
     * Get player Hyris money
     *
     * @return A {@link IHyriMoney} instance
     */
    IHyriMoney getHyris();

    /**
     * Get player Hyodes money
     *
     * @return A {@link IHyriMoney} instance
     */
    IHyriMoney getHyodes();

    /**
     * Get the player leveling on the network
     *
     * @return A {@link IHyriLeveling} instance
     */
    NetworkLeveling getNetworkLeveling();

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
     * Get the guild of the player
     *
     * @return THe id of the guild
     */
    @NotNull ObjectId getGuild();

    /**
     * Set the guild of the player
     *
     * @param guild The id of the guild
     */
    void setGuild(@NotNull ObjectId guild);

    /**
     * Get the friends module of the player
     *
     * @return The {@link IHyriFriendsModule} instance
     */
    @NotNull IHyriFriendsModule getFriends();

    /**
     * Get the authentication module of the player
     *
     * @return The {@link IHyriAuthModule} instance
     */
    @NotNull IHyriAuthModule getAuth();

    /**
     * Get the hosts module of the player
     *
     * @return The {@link IHyriPlayerHostModule} instance
     */
    @NotNull IHyriPlayerHostModule getHosts();

    /**
     * Get the statistics handler of the player
     *
     * @return The {@linkplain IHyriStatisticsModule} instance
     */
    @NotNull IHyriStatisticsModule getStatistics();

    /**
     * Get the data handler of the player
     *
     * @return The {@linkplain IHyriPlayerDataModule} instance
     */
    @NotNull IHyriPlayerDataModule getData();

    /**
     * Get the transactions handler of the player
     *
     * @return The {@linkplain IHyriTransactionsModule} instance
     */
    @NotNull IHyriTransactionsModule getTransactions();

    /**
     * Update the player account in cache
     */
    default void update() {
        HyriAPI.get().getPlayerManager().savePlayer(this);
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