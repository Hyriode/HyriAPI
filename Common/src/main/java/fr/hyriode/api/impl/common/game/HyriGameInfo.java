package fr.hyriode.api.impl.common.game;

import fr.hyriode.api.game.HyriGameType;
import fr.hyriode.api.game.IHyriGameInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:54
 */
public class HyriGameInfo implements IHyriGameInfo {

    private final String name;
    private String displayName;
    private final Map<String, HyriGameType> types;

    public HyriGameInfo(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
        this.types = new HashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public HyriGameType getType(String name) {
        return this.types.get(name);
    }

    @Override
    public void addType(String name, HyriGameType type) {
        this.types.put(name, type);
    }

    @Override
    public void removeType(String name) {
        this.types.remove(name);
    }

    @Override
    public Map<String, HyriGameType> getTypes() {
        return this.types;
    }

}
