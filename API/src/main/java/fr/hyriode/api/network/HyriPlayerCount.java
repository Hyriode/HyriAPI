package fr.hyriode.api.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:37
 */
public class HyriPlayerCount {

    /** The amount of players on the network */
    private final int players;
    /** The count of players in each game */
    private final Map<String, HyriGameCount> games;

    /**
     * Constructor of {@link HyriPlayerCount}
     *
     * @param players A player amount
     */
    public HyriPlayerCount(int players) {
        this.players = players;
        this.games = new HashMap<>();
    }

    /**
     * Get the amount of players playing on the network
     *
     * @return A player amount
     */
    public int getPlayers() {
        return this.players;
    }

    /**
     * Set a game count
     *
     * @param game The game
     * @param count The count
     */
    public void setGameCount(String game, HyriGameCount count) {
        this.games.put(game, count);
    }

    /**
     * Get a game count
     *
     * @param game The game
     * @return A {@link HyriGameCount} linked to the game
     */
    public HyriGameCount getGameCount(String game) {
        return this.games.get(game);
    }

    /**
     * Get the games counts
     *
     * @return A map
     */
    public Map<String, HyriGameCount> getGames() {
        return this.games;
    }

}
