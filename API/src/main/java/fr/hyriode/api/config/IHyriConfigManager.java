package fr.hyriode.api.config;

/**
 * Created by AstFaster
 * on 20/02/2023 at 10:58.<br>
 *
 * The manager of {@linkplain IHyriConfig configs} stored in database.
 */
public interface IHyriConfigManager {

    /** The default category for configs */
    String DEFAULT_CATEGORY = "DEFAULT";

    /**
     * Get a config stored in database
     *
     * @param clazz The class of the config to deserialize
     * @param database The database where the config is stored (e.g. bedwars)
     * @param category The category of the config (e.g. SOLO)
     * @param name The name of the config (e.g. the name of the map)
     * @return The found {@linkplain IHyriConfig config}
     * @param <T> The type of config to return
     */
    <T extends IHyriConfig> T getConfig(Class<T> clazz, String database, String category, String name);

    /**
     * Get a config stored in database.<br>
     * This method used {@link IHyriConfigManager#DEFAULT_CATEGORY} as a category.
     *
     * @param clazz The class of the config to deserialize
     * @param database The database where the config is stored (e.g. bedwars)
     * @param name The name of the config (e.g. the name of the map)
     * @return The found {@linkplain IHyriConfig config}
     * @param <T> The type of config to return
     */
    <T extends IHyriConfig> T getConfig(Class<T> clazz, String database, String name);

    /**
     * Save a config in database
     *
     * @param config The config to save
     * @param database The database where the config will be stored (e.g. bedwars)
     * @param category The category of the config (e.g. SOLO)
     * @param name The name of the config (e.g. the name of the map)
     */
    void saveConfig(IHyriConfig config, String database, String category, String name);

    /**
     * Save a config in database.<br>
     * This method used {@link IHyriConfigManager#DEFAULT_CATEGORY} as a category.
     *
     * @param config The config to save
     * @param database The database where the config will be stored (e.g. bedwars)
     * @param name The name of the config (e.g. the name of the map)
     */
    void saveConfig(IHyriConfig config, String database, String name);

    /**
     * Delete a config from database
     *
     * @param database The database where the config stored (e.g. bedwars)
     * @param category The category of the config (e.g. SOLO)
     * @param name The name of the config (e.g. the name of the map)
     */
    void deleteConfig(String database, String category, String name);

    /**
     * Delete a config from database.<br>
     * This method used {@link IHyriConfigManager#DEFAULT_CATEGORY} as a category.
     *
     * @param database The database where the config stored (e.g. bedwars)
     * @param name The name of the config (e.g. the name of the map)
     */
    void deleteConfig(String database, String name);

}
