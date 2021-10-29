package fr.hyriode.hyriapi.cosmetics;

import java.util.List;
import java.util.UUID;

public interface IHyriCosmeticsManager {

    /**
     * Get a cosmetic with his name
     * @param name The name of the cosmetic
     * @return The cosmetic with the given name
     */
    HyriCosmetic getCosmetic(String name);

    /**
     * Get a cosmetic with his Class
     * @param cosmetic The class of the Cosmetic
     * @return The cosmetic registered with the given class
     */
    HyriCosmetic getCosmetic(Class<? extends HyriCosmetic> cosmetic);

    /**
     * Register a cosmetic
     * @param cosmetic The cosmetic to register
     */
    void registerCosmetic(Class<? extends HyriCosmetic> cosmetic);


    /**
     * Get the cosmetics of a given player
     * @param uuid The player UUID
     * @return A list of owned cosmetics
     */
    List<Class<? extends HyriCosmetic>> getCosmetics(UUID uuid);

    /**
     * Check if player has a cosmetic
     * @param cosmetic The wanted cosmetic
     * @param uuid The player UUID
     * @return True if player has the given cosmetic
     */
    boolean hasCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid);

    /**
     * Add a cosmetic to a player
     * @param cosmetic The cosmetic to give
     * @param uuid The player to give cosmetic
     */
    void addCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid);

    /**
     * Add multiples cosmetics to a player
     * @param cosmetics List of cosmetics to give
     * @param uuid The player to give cosmetics
     */
    void addCosmetics(List<Class<? extends HyriCosmetic>> cosmetics, UUID uuid);

    /**
     * Remove a cosmetic to a player
     * @param cosmetic The cosmetic to remove
     * @param uuid The player to remove cosmetic
     */
    void removeCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid);

    /**
     * Remove multiples cosmetics to a player
     * @param cosmetics List of cosmetics to remove
     * @param uuid The player to remove cosmetics
     */
    void removeCosmetics(List<Class<? extends HyriCosmetic>> cosmetics, UUID uuid);

    /**
     * Get all registered cosmetics
     * @return The map of all registered cosmetics
     */
    List<Class<? extends HyriCosmetic>> getRegisteredCosmetics();
}
