package fr.hyriode.api.language;

import fr.hyriode.api.player.IHyriPlayer;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
     * Get the message's value in a language by giving the message key
     *
     * @param language Value language
     * @param messageKey Message key
     * @return Message value
     */
    String getValue(HyriLanguage language, String messageKey);

    /**
     * Get the message's value for a player by giving the message key
     *
     * @param playerId Player {@link UUID}
     * @param messageKey Message key
     * @return Message value
     */
    String getValue(UUID playerId, String messageKey);

    /**
     * Get the message's value for a player by giving the message key
     *
     * @param account Player account
     * @param messageKey Message key
     * @return Message value
     */
    String getValue(IHyriPlayer account, String messageKey);

    /**
     * Get the message's value for a player by giving the message key
     *
     * @param languagePlayer The language player
     * @param messageKey Message key
     * @return Message value
     */
    String getValue(IHyriLanguagePlayer languagePlayer, String messageKey);

    /**
     * Get all loaded messages
     *
     * @return A {@link Set} of messages
     */
    Set<HyriLanguageMessage> getMessages();

}
