package fr.hyriode.api.game.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.game.IHyriGameInfo;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/04/2022 at 07:04
 */
public class HyriGameInfoRegisteredEvent extends HyriEvent {

    private final IHyriGameInfo gameInfo;

    public HyriGameInfoRegisteredEvent(IHyriGameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public IHyriGameInfo getGameInfo() {
        return this.gameInfo;
    }

}
