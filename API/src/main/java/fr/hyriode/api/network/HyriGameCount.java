package fr.hyriode.api.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:39
 */
public class HyriGameCount {

    /** The amount of players playing the game */
    private final int players;
    /** The count of players in each game type */
    private final Map<String, Integer> types;

    /**
     * Constructor of {@link HyriGameCount}
     *
     * @param players
     */
    public HyriGameCount(int players) {
        this.players = players;
        this.types = new HashMap<>();
    }

    /**
     * Get the amount of players playing the game
     *
     * @return A player amount
     */
    public int getPlayers() {
        return this.players;
    }

    /**
     * Set the amount of players on a game type
     *
     * @param type The type of the game
     * @param players The amount of players
     */
    public void setType(String type, int players) {
        this.types.put(type, players);
    }

    /**
     * Get the amount of players on a game type
     *
     * @param type The game type
     * @return The amount of players
     */
    public int getType(String type) {
        return this.types.get(type);
    }

    /**
     * Get the player amount of each type
     *
     * @return A map
     */
    public Map<String, Integer> getTypes() {
        return this.types;
    }

}
