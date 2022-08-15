package fr.hyriode.api.game.rotating;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.game.IHyriGameInfo;

/**
 * Created by AstFaster
 * on 27/07/2022 at 13:00
 */
public class RotatingGameChangedEvent extends HyriEvent {

    private final String game;

    public RotatingGameChangedEvent(String game) {
        this.game = game;
    }

    public String getGameName() {
        return this.game;
    }

    public IHyriRotatingGame getGame() {
        return HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame();
    }

    public IHyriGameInfo getGameInfo() {
        return this.getGame().getInfo();
    }

}
