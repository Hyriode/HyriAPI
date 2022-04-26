package fr.hyriode.api.game;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:52
 */
public class HyriGameType {

    private final int id;
    private final String displayName;

    public HyriGameType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
