package fr.hyriode.api.game;

import fr.hyriode.api.HyriAPI;

import java.util.List;

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
     * @return A {@linkplain IHyriGameType game type} object
     */
    IHyriGameType getType(String name);

    /**
     * Add a type to the game
     *
     * @param id The id of the type to add
     * @param name The name of the type to add
     * @param displayName The display name of the type to add
     */
    void addType(int id, String name, String displayName);

    /**
     * Remove a type from the game
     *
     * @param name The name of the type to remove
     */
    void removeType(String name);

    /**
     * Get the types of the game
     *
     * @return A list of {@linkplain IHyriGameType game type}
     */
    List<IHyriGameType> getTypes();

    /**
     * Update the game info
     */
    default void update() {
        HyriAPI.get().getGameManager().saveGameInfo(this);
    }

}
