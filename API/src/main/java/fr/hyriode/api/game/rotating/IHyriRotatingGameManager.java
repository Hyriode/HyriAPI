package fr.hyriode.api.game.rotating;

import fr.hyriode.api.game.IHyriGameInfo;

import java.util.List;

/**
 * Created by AstFaster
 * on 27/07/2022 at 08:19
 */
public interface IHyriRotatingGameManager {

    /**
     * Get the current rotating game
     *
     * @return A {@link IHyriRotatingGame}; or <code>null</code> if there is no rotating game
     */
    IHyriRotatingGame getRotatingGame();

    /**
     * Add a rotating game with its priority of appearance.<br>
     * It will shift all the games with a higher priority to the right.
     *
     * @param order The order of the game (0 is high, 10 is low for example)
     * @param game The game to add as rotating
     */
    void addRotatingGame(int order, String game);

    /**
     * Remove a rotating game
     *
     * @param game The game to remove
     */
    void removeRotatingGame(String game);

    /**
     * Switch to the next rotating game
     */
    void switchToNextRotatingGame();

    /**
     * Get the priority of a game
     *
     * @param game The game name
     * @return The priority of the game; or -1 if no rotating game exists with the given name
     */
    int getOrder(String game);

    /**
     * Get a list of rotating games sorted by their order of appearance on the server<br>
     * The next game will have the index 0, the next one 1, the next one 2, etc.
     *
     * @return A list of {@link IHyriGameInfo}
     */
    List<IHyriGameInfo> getRotatingGames();

}
