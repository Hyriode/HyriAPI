package fr.hyriode.api.game.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.game.IHyriGameInfo;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/04/2022 at 07:04
 */
public class HyriGameInfoUnregisteredEvent extends HyriEvent {

    private final String gameName;

    public HyriGameInfoUnregisteredEvent(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return this.gameName;
    }

}
