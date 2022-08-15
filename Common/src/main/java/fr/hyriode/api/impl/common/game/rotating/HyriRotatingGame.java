package fr.hyriode.api.impl.common.game.rotating;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.rotating.IHyriRotatingGame;

/**
 * Created by AstFaster
 * on 27/07/2022 at 12:07
 */
public class HyriRotatingGame implements IHyriRotatingGame {

    private final String gameName;
    private final long sinceWhen;

    public HyriRotatingGame(String gameName, long sinceWhen) {
        this.gameName = gameName;
        this.sinceWhen = sinceWhen;
    }

    @Override
    public IHyriGameInfo getInfo() {
        return HyriAPI.get().getGameManager().getGameInfo(this.gameName);
    }

    @Override
    public long sinceWhen() {
        return this.sinceWhen;
    }

}
