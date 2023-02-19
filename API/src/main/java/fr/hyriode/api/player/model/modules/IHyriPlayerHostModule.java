package fr.hyriode.api.player.model.modules;

import fr.hyriode.api.player.IHyriPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 17/02/2023 at 15:08.<br>
 *
 * The module used to manage the data linked to the hosts system for a {@linkplain IHyriPlayer player}.
 */
public interface IHyriPlayerHostModule {

    /**
     * Get the available hosts of the player
     *
     * @return The available hosts
     */
    int getAvailableHosts();

    /**
     * Add an amount of available hosts
     *
     * @param hosts The amount of available hosts to add
     */
    void addAvailableHosts(int hosts);

    /**
     * Set the amount of available hosts
     *
     * @param hosts The new amount of available hosts
     */
    void setAvailableHosts(int hosts);

    /**
     * Get the players banned from the player's hosts
     *
     * @return A list of player {@link UUID}
     */
    @NotNull Set<UUID> getBannedPlayers();

    /**
     * Add a player banned from the player's hosts
     *
     * @param playerId The {@link UUID} of the player to ban
     */
    void addBannedPlayer(@NotNull UUID playerId);

    /**
     * Remove a player banned from the player's hosts
     *
     * @param playerId The {@link UUID} of the player to unban
     */
    void removeBannedPlayer(@NotNull UUID playerId);

    /**
     * Check whether a player is banned from the player's hosts
     *
     * @param playerId The {@link UUID} of the player to check
     * @return <code>true</code> if the given player is banned
     */
    boolean hasBannedPlayer(@NotNull UUID playerId);

    /**
     * Get the favorite host configs of the player
     *
     * @return A list of config id
     */
    @NotNull Set<String> getFavoriteConfigs();

    /**
     * Add a favorite host config
     *
     * @param config The config to add
     */
    void addFavoriteConfig(@NotNull String config);

    /**
     * Remove a favorite host config
     *
     * @param config The config to remove
     */
    void removeFavoriteConfig(@NotNull String config);

    /**
     * Check whether a host config is in the favorites ones
     *
     * @param config The config to check
     * @return <code>true</code> if the config is in the favorites ones
     */
    boolean hasFavoriteConfig(@NotNull String config);

}
