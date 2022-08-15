package fr.hyriode.api.host;

import fr.hyriode.api.HyriAPI;

import java.util.Map;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 07/08/2022 at 12:47
 */
public interface IHostConfig {

    /**
     * Get the identifier of the config
     *
     * @return An identifier
     */
    String getId();

    /**
     * Get the time when the config has been created
     *
     * @return A millisecond timestamp
     */
    long getCreationDate();

    /**
     * Get the owner of the config
     *
     * @return The {@link UUID} of the config's owner
     */
    UUID getOwner();

    /**
     * Get with which game the config is linked to
     *
     * @return A game
     */
    String getGame();

    /**
     * Get the type of the game the config is linked to
     *
     * @return A game type
     */
    String getGameType();

    /**
     * Get the name of the config
     *
     * @return A name
     */
    String getName();

    /**
     * Set the name of the config
     *
     * @param name The new name of the config
     */
    void setName(String name);

    /**
     * Get the icon of the config
     *
     * @return The config's icon
     */
    String getIcon();

    /**
     * Set the icon of the config
     *
     * @param icon The new icon of the config
     */
    void setIcon(String icon);

    /**
     * Check if the config is private
     *
     * @return <code>true</code> whether the config is private or not
     */
    boolean isPrivate();

    /**
     * Set if the config is private
     *
     * @param value New value for config privacy
     */
    void setPrivate(boolean value);

    /**
     * Add a value in the config
     *
     * @param key The key of the value
     * @param object The value
     */
    void addValue(String key, Object object);

    /**
     * Remove value from the config by giving its key
     *
     * @param key The key of the value to remove
     */
    void removeValue(String key);

    /**
     * Get a value from its key
     *
     * @param key The key of the value to get
     * @return An object; or <code>null</code> if no value exists with the given key
     */
    Object getValue(String key);

    /**
     * Get all the values stored in the config
     *
     * @return A {@link Map} of values linked to their key
     */
    Map<String, Object> getValues();

    /**
     * Save the config
     */
    default void save() {
        HyriAPI.get().getHostConfigManager().saveConfig(this);
    }

    /**
     * Delete the config
     */
    default void delete() {
        HyriAPI.get().getHostConfigManager().deleteConfig(this);
    }

}
