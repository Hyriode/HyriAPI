package fr.hyriode.api.game;

import fr.hyriode.api.HyriAPI;

import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:46
 */
public interface IHyriGameInfo {

    /**
     * Get the name of the game.<br>
     * Example: bedwars, sheepwars, etc.
     *
     * @return A name
     */
    String getName();

    /**
     * Get the display name of the game.<br>
     * Example: BedWars, SheepWars, etc.
     *
     * @return A display name
     */
    String getDisplayName();

    /**
     * Set the new display name of the game
     *
     * @param displayName A display name
     */
    void setDisplayName(String displayName);

    /**
     * Get a game type by giving its name
     *
     * @param name The name of the type to get
     * @return A {@linkplain HyriGameType game type} object
     */
    HyriGameType getType(String name);

    /**
     * Add a type to the game
     *
     * @param name The name of the type to add
     * @param type The type object
     */
    void addType(String name, HyriGameType type);

    /**
     * Remove a type from the game
     *
     * @param name The name of the type to remove
     */
    void removeType(String name);

    /**
     * Get all the types of the game
     *
     * @return A map of {@link HyriGameType}
     */
    Map<String, HyriGameType> getTypes();

    /**
     * Update the game info
     */
    default void update() {
        HyriAPI.get().getGameManager().saveGameInfo(this);
    }

}
