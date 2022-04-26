package fr.hyriode.api.game;

import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:50
 */
public interface IHyriGameManager {

    IHyriGameInfo createGameInfo(String name, String displayName);

    IHyriGameInfo getGameInfo(String name);

    void saveGameInfo(IHyriGameInfo game);

    void deleteGameInfo(String gameName);

    List<IHyriGameInfo> getGamesInfo();

    List<String> getMaps(String game, String gameType);

}
