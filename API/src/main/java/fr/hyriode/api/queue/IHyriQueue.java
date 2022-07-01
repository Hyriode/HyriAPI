package fr.hyriode.api.queue;

/**
 * Created by AstFaster
 * on 30/06/2022 at 10:40
 */
public interface IHyriQueue {

    /**
     * Get the game of the queue
     *
     * @return A game name
     */
    String getGame();

    /**
     * Get the type of the game
     *
     * @return A game type
     */
    String getGameType();

    /**
     * Get the map of the game
     *
     * @return A map; can be null
     */
    String getMap();

}
