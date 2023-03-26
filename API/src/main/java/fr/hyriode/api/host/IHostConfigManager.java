package fr.hyriode.api.host;

import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 07/08/2022 at 12:50
 */
public interface IHostConfigManager {

    /**
     * Create a host config
     *
     * @param owner The owner of the config
     * @param game The game linked to the config
     * @param gameType The type of the game linked to the config
     * @param name The name of the config
     * @param icon The icon of the config
     * @return The created {@link IHostConfig}
     */
    IHostConfig createConfig(UUID owner, String game, String gameType, String name, String icon);

    /**
     * Save a given config
     *
     * @param config The config to save
     */
    void saveConfig(IHostConfig config);

    /**
     * Delete a given config
     *
     * @param config The config to delete
     */
    void deleteConfig(IHostConfig config);

    /**
     * Get a {@linkplain IHostConfig host config} from its id
     *
     * @param id The identifier of the config
     * @return The {@link IHostConfig} linked to the given id; or <code>null</code> if no config has been found
     */
    IHostConfig getConfig(String id);

    /**
     * Check whether a given config id exists or not
     *
     * @param id The id of the config to check
     * @return <code>true</code> if the config exists
     */
    boolean existsConfig(String id);

    /**
     * Get the total of config loadings
     *
     * @param id The identifier of the config
     * @return A total
     */
    long getConfigLoadings(String id);

    /**
     * Add a config loading to a config
     *
     * @param id The identifier of the config
     */
    void addConfigLoading(String id);

    /**
     * Get all the configs of a given player
     *
     * @param playerId The {@link UUID} of the player
     * @return A list of config id; or an empty list if the player doesn't have any config
     */
    List<String> getPlayerConfigs(UUID playerId);

    /**
     * Get all the configs available on the server
     *
     * @param start The start of the range
     * @param stop The end of the range
     * @return A list of config id
     */
    List<String> getConfigs(long start, long stop);

}
