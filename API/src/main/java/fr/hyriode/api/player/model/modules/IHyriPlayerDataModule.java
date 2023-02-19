package fr.hyriode.api.player.model.modules;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlayerData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by AstFaster
 * on 17/02/2023 at 09:59.<br>
 *
 * The module used to manage the {@linkplain IHyriPlayerData data} inside a {@linkplain IHyriPlayer player account}.
 */
public interface IHyriPlayerDataModule {

    /**
     * Get all keys of data stored
     *
     * @return A list of key
     */
    @NotNull Set<String> keys();

    /**
     * Add a new data to the player's account
     *
     * @param key The key of the data
     * @param data The data to add
     */
    void add(@NotNull String key, @NotNull IHyriPlayerData data);

    /**
     * Remove a data from the player's account
     *
     * @param key The key of the data to remove
     */
    void remove(@NotNull String key);

    /**
     * Get a data from its key.<br>
     * Before getting a {@link IHyriPlayerData} you must have read it with {@link IHyriPlayerDataModule#read(String, IHyriPlayerData)} or just adding it first.
     *
     * @param key The key of the data to get
     * @return The found data if one is linked to the given key
     * @param <T> The type of data to return
     */
    <T extends IHyriPlayerData> T get(@NotNull String key);

    /**
     * Read a data from its key.<br>
     * Data are not deserialized immediately, so you must do it yourself.
     *
     * @param key The key of the data to read
     * @param emptyData The empty data object that will read the data (as bytes)
     * @return The read {@link IHyriPlayerData} if one is linked to the given key
     * @param <T> The type of data to return
     */
    <T extends IHyriPlayerData> T read(@NotNull String key, @NotNull T emptyData);

    /**
     * Check whether a data exists for a given key
     *
     * @param key The key of the data to check
     * @return <code>true</code> if a data exists for the given key
     */
    boolean has(@NotNull String key);

}
