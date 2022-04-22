package fr.hyriode.api.game;

import fr.hyriode.api.HyriAPI;

import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:46
 */
public interface IHyriGameInfo {

    String getName();

    String getDisplayName();

    HyriGameType getType(String name);

    void addType(String name, HyriGameType type);

    void removeType(String name);

    Map<String, HyriGameType> getTypes();

    default void update() {
        HyriAPI.get().getGameManager().saveGameInfo(this);
    }

}
