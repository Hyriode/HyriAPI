package fr.hyriode.api.settings;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:18
 */
public interface IHyriSettingsManager {

    /**
     * Get settings of a player by giving player uuid
     *
     * @param uuid - Player {@link UUID}
     * @return - Player settings
     */
    IHyriPlayerSettings getPlayerSettings(UUID uuid);

    /**
     * Create basic settings
     *
     * @return - Basic settings
     */
    IHyriPlayerSettings createPlayerSettings();

    /**
     * Reset the settings of a given player
     *
     * @param player - The player
     */
    void resetPlayerSettings(IHyriPlayer player);

}
