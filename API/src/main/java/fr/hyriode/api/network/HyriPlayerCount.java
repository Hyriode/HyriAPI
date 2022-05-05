package fr.hyriode.api.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:39
 */
public class HyriPlayerCount {

    /** The count of players in each type. It can be empty is the counter is not for a game */
    private final Map<String, Integer> types = new HashMap<>();

    /**
     * Get the amount of players playing the category
     *
     * @return A player amount
     */
    public int getPlayers() {
        int players = 0;

        for (int amount : this.types.values()) {
            players += amount;
        }
        return players;
    }

    /**
     * Set the amount of players on a type
     *
     * @param type The type of the server
     * @param players The amount of players
     */
    public void setType(String type, int players) {
        this.types.put(type, players);
    }

    /**
     * Get the amount of players on a type
     *
     * @param type The server type
     * @return The amount of players
     */
    public int getType(String type) {
        if (this.types.containsKey(type)) {
            return this.types.get(type);
        }
        return 0;
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
