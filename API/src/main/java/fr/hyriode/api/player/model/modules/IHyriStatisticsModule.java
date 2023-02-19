package fr.hyriode.api.player.model.modules;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by AstFaster
 * on 17/02/2023 at 09:59.<br>
 *
 * The module used to manage the {@linkplain IHyriStatistics statistics} inside a {@linkplain IHyriPlayer player account}.
 */
public interface IHyriStatisticsModule {

    /**
     * Get the total play time of the player on the network
     *
     * @return A play time (in seconds)
     */
    long getTotalPlayTime();

    /**
     * Get the play time of the player in a category (e.g. bedwars, lobby, etc)
     *
     * @param category The category of the play time to get
     * @return A play time (in seconds)
     */
    long getPlayTime(@NotNull String category);

    /**
     * Add a duration to the current play time of the player in a category
     *
     * @param category The category of the play time to add
     * @param playTime The play time to add (in seconds)
     */
    void addPlayTime(@NotNull String category, long playTime);

    /**
     * Get all keys of statistics stored
     *
     * @return A list of key
     */
    @NotNull Set<String> keys();

    /**
     * Add new statistics to the player's account
     *
     * @param key The key of the statistics
     * @param statistics The statistics to add
     */
    void add(@NotNull String key, @NotNull IHyriStatistics statistics);

    /**
     * Remove statistics from the player's account
     *
     * @param key The key of the statistics to remove
     */
    void remove(@NotNull String key);

    /**
     * Get statistics from their key.<br>
     * Before getting a {@link IHyriStatistics} you must have read it with {@link IHyriStatisticsModule#read(String, IHyriStatistics)}.
     *
     * @param key The key of the statistics to get
     * @return The found statistics if one is linked to the given key
     * @param <T> The type of statistics to return
     */
    <T extends IHyriStatistics> T get(@NotNull String key);

    /**
     * Read statistics from their key.<br>
     * Statistics are not deserialized immediately, so you must do it yourself.
     *
     * @param key The key of the statistics to read
     * @param emptyData The empty statistics object that will read the statistics (as bytes)
     * @return The read {@link IHyriStatistics} if one is linked to the given key
     * @param <T> The type of statistics to return
     */
    <T extends IHyriStatistics> T read(@NotNull String key, @NotNull T emptyData);

    /**
     * Check whether statistics exists for a given key
     *
     * @param key The key of the statistics to check
     * @return <code>true</code> if a statistics exists for the given key
     */
    boolean has(@NotNull String key);

}
