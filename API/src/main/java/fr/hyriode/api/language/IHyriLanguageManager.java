package fr.hyriode.api.language;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by AstFaster
 * on 14/07/2022 at 22:22
 */
public interface IHyriLanguageManager {

    /**
     * Load all languages messages from languages files that are in the given folder.<br>
     * Languages files have to be in the following format: 'language_code'.json. Some examples: en.json, fr.json, es.json...
     *
     * @param folder The folder that contains language files
     * @return The list of all messages loaded
     */
    List<HyriLanguageMessage> loadLanguagesMessages(File folder);

    /**
     * Register an adapter for a given object class
     *
     * @param clazz The class of the object
     * @param adapter The adapter to register
     * @param <T> The type linked with the class and the adapter
     */
    <T> void registerAdapter(Class<T> clazz, IHyriLanguageAdapter<T> adapter);

    /**
     * Add a language message
     *
     * @param message Message to add
     */
    void addMessage(HyriLanguageMessage message);

    /**
     * Remove a language message
     *
     * @param message Message to remove
     */
    void removeMessage(HyriLanguageMessage message);

    /**
     * Remove a language message from its key
     *
     * @param key Message's key to remove
     */
    void removeMessage(String key);

    /**
     * Get a message from a provided key
     *
     * @param key Message key
     * @return The message
     */
    HyriLanguageMessage getMessage(String key);

    /**
     * Get all loaded messages
     *
     * @return A {@link Set} of messages
     */
    Set<HyriLanguageMessage> getMessages();

    /**
     * Get an adapter instance by its linked class
     *
     * @param clazz The class linked to the adapter
     * @return A {@link IHyriLanguageAdapter} or <code>null</code> if no adapter is linked to the given class
     * @param <T> The type of adapter to return
     */
    <T> IHyriLanguageAdapter<T> getAdapter(Class<T> clazz);

    /**
     * Get all the registered adapters
     *
     * @return The map of {@link IHyriLanguageAdapter}
     */
    Map<Class<?>, IHyriLanguageAdapter<?>> getAdapters();

}
