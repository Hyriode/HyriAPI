package fr.hyriode.api.util;

import fr.hyriode.api.HyriAPI;

import java.util.HashMap;

/**
 * Created by AstFaster
 * on 23/11/2022 at 18:39.<br>
 *
 * A dictionary storing useful data.
 */
public class DataDictionary extends HashMap<String, String> {

    /**
     * Add a given data to the dictionary
     *
     * @param key The key of the data
     * @param data The data to add (as {@link String})
     * @return This {@link DataDictionary} instance
     */
    public DataDictionary add(String key, String data) {
        super.put(key, data);
        return this;
    }

    /**
     * Add a given data to the dictionary.<br>
     * The given data need to be serializable in JSON format.
     *
     * @param key The key of the data
     * @param data The data to add (as an {@link Object})
     * @return This {@link DataDictionary} instance
     */
    public DataDictionary add(String key, Object data) {
        super.put(key, HyriAPI.GSON.toJson(data));
        return this;
    }

    /**
     * Get a given data by its key
     *
     * @param key The key of the data to get
     * @param objectClass The class of the data to get
     * @return The found data; or <code>null</code> if nothing was found
     * @param <T> The type of the result
     */
    public <T> T get(String key, Class<T> objectClass) {
        final String json = this.get(key);

        return json == null ? null : HyriAPI.GSON.fromJson(json, objectClass);
    }

    /**
     * Remove a data from the dictionary
     *
     * @param key The key of the data to remove
     * @return This {@link DataDictionary} instance
     */
    public DataDictionary remove(String key) {
        super.remove(key);
        return this;
    }

    /**
     * Check whether the dictionary contains a given data or not.
     *
     * @param key The key of the data
     * @return <code>true</code> if it contains the data
     */
    public boolean contains(String key) {
        return this.containsKey(key);
    }

}
