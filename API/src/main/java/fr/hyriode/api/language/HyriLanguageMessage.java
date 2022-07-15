package fr.hyriode.api.language;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Project: Hyrame
 * Created by AstFaster
 * on 31/08/2021 at 20:32
 */
public class HyriLanguageMessage {

    /** All the possible values for languages */
    private Map<HyriLanguage, String> values = new HashMap<>();

    /** The key of the message. For example: gui.team.name */
    private String key;

    /**
     * Constructor of {@link HyriLanguageMessage}
     *
     * @param key The key of the message
     */
    public HyriLanguageMessage(String key) {
        this.key = key;
    }

    /**
     * Get the message's key
     *
     * @return - Message's key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Set the message's key
     *
     * @param key - New key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Add a value to message
     *
     * @param HyriLanguage - Value's language
     * @param value - Value to add
     * @return - This message (useful to make inline pattern)
     */
    public HyriLanguageMessage addValue(HyriLanguage HyriLanguage, String value) {
        this.values.put(HyriLanguage, value);
        return this;
    }

    /**
     * Remove a value from the message
     *
     * @param HyriLanguage - Value's HyriLanguage to remove
     * @return - This message (useful to make inline pattern)
     */
    public HyriLanguageMessage removeValue(HyriLanguage HyriLanguage) {
        this.values.remove(HyriLanguage);
        return this;
    }

    /**
     * Set the values of the message
     *
     * @param values - Values
     * @return {@link HyriLanguageMessage}
     */
    public HyriLanguageMessage setValues(Map<HyriLanguage, String> values) {
        this.values = values;
        return this;
    }

    /**
     * Get all the values of the message
     *
     * @return - Values
     */
    public Map<HyriLanguage, String> getValues() {
        return this.values;
    }

    /**
     * Get the value of a value
     *
     * @param language The language
     * @return The value linked to the provided language
     */
    public String getValue(HyriLanguage language) {
        return this.values.getOrDefault(language, this.values.size() > 0 ? new ArrayList<>(this.values.values()).get(0) : "?");
    }

    /**
     * Get the value of the message for a given player
     *
     * @param player The account of the player
     * @return The value that correspond to the player
     */
    public String getForPlayer(IHyriPlayer player) {
        return this.getValue(player.getSettings().getLanguage());
    }

    /**
     * Get the value of the message for a given player
     *
     * @param playerId The identifier of the player
     * @return The value that correspond to the player
     */
    public String getForPlayer(UUID playerId) {
        return this.getForPlayer(IHyriPlayer.get(playerId));
    }

    /**
     * Get the value of the message for a given player
     *
     * @param languagePlayer A language player
     * @return The value that correspond to the player
     */
    public String getForPlayer(IHyriLanguagePlayer languagePlayer) {
        return this.getForPlayer(languagePlayer.getAccount());
    }

    /**
     * Create a {@link HyriLanguageMessage} from a single string value
     *
     * @param value The value
     * @return The created {@link HyriLanguageMessage}
     */
    public static HyriLanguageMessage from(String value) {
        return new HyriLanguageMessage("").addValue(HyriLanguage.FR, value);
    }

    /**
     * Create a {@link HyriLanguageMessage} from values
     *
     * @param values Map with all values
     * @return The created {@link HyriLanguageMessage}
     */
    public static HyriLanguageMessage from(Map<HyriLanguage, String> values) {
        return new HyriLanguageMessage("").setValues(values);
    }

    /**
     * Get a {@link HyriLanguageMessage} from its key
     *
     * @param key The key of the message to get
     * @return A {@link HyriLanguageMessage}
     */
    public static HyriLanguageMessage get(String key) {
        return HyriAPI.get().getLanguageManager().getMessage(key);
    }

}