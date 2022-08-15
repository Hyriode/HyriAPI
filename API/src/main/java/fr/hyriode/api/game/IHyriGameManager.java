package fr.hyriode.api.game;

import fr.hyriode.api.game.rotating.IHyriRotatingGameManager;

import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:50
 */
public interface IHyriGameManager {

    /**
     * Create a game info object
     *
     * @param name The name of the game info to create
     * @param displayName The display name of the game info to create
     * @return The created {@link IHyriGameInfo}
     */
    IHyriGameInfo createGameInfo(String name, String displayName);

    /**
     * Get a game info by its name
     *
     * @param name The name of the game info to get
     * @return A {@link IHyriGameInfo} object; or <code>null</code> if no game info was found
     */
    IHyriGameInfo getGameInfo(String name);

    /**
     * Save a game info
     *
     * @param game The game info to save
     */
    void saveGameInfo(IHyriGameInfo game);

    /**
     * Delete a game info
     *
     * @param gameName The name of the game info to delete
     */
    void deleteGameInfo(String gameName);

    /**
     * Get all the game info
     *
     * @return A list of {@link IHyriGameInfo}
     */
    List<IHyriGameInfo> getGamesInfo();

    /**
     * Get all maps of a game with a specific game type
     *
     * @param game The name of the game
     * @param gameType The type of the game
     * @return A list of map name
     */
    List<String> getMaps(String game, String gameType);

    /**
     * Get the {@linkplain IHyriRotatingGameManager rotating game manager} instance
     *
     * @return The {@link IHyriRotatingGameManager} instance
     */
    IHyriRotatingGameManager getRotatingGameManager();

}
