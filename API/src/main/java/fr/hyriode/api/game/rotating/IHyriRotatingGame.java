package fr.hyriode.api.game.rotating;

import fr.hyriode.api.game.IHyriGameInfo;

/**
 * Created by AstFaster
 * on 27/07/2022 at 08:27
 */
public interface IHyriRotatingGame {

    /**
     * Get the information about the rotating game
     *
     * @return A {@link IHyriGameInfo} object linked to the rotating game
     */
    IHyriGameInfo getInfo();

    /**
     * Get since when it appeared on the server
     *
     * @return A timestamp
     */
    long sinceWhen();

}
