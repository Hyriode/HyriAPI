package fr.hyriode.api.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:37
 */
public class HyriNetworkCount {

    /** The amount of players on the network */
    private int players;
    /** The count of players in each game */
    private final Map<String, HyriPlayerCount> categories;

    /**
     * Constructor of {@link HyriNetworkCount}
     *
     */
    public HyriNetworkCount() {
        this.categories = new HashMap<>();
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
     * Set players amount on the network
     *
     * @param players New players amount
     */
    public void setPlayers(int players) {
        this.players = players;
    }

    /**
     * Add a given amount of players
     *
     * @param players Players amount
     */
    public void addPlayers(int players) {
        this.players += players;
    }


    /**
     * Remove a given amount of players
     *
     * @param players Players amount
     */
    public void removePlayers(int players) {
        this.players -= players;
    }

    /**
     * Set a category player count
     *
     * @param category The category name
     * @param count The count
     */
    public void setCategory(String category, HyriPlayerCount count) {
        this.categories.put(category, count);
    }

    /**
     * Get a category count
     *
     * @param category The category name
     * @return A {@link HyriPlayerCount} linked to the game
     */
    public HyriPlayerCount getCategory(String category) {
        return this.categories.get(category);
    }

    /**
     * Get all the categories
     *
     * @return A map of {@link HyriPlayerCount}
     */
    public Map<String, HyriPlayerCount> getCategories() {
        return this.categories;
    }

}
