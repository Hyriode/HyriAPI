package fr.hyriode.hyriapi.cosmetics;

import fr.hyriode.hyriapi.player.IHyriPlayer;

import java.util.List;
import java.util.UUID;

public interface IHyriCosmeticsManager {

    /**
     * Get a cosmetic with his name
     * @param name The name of the cosmetic
     * @return The cosmetic with the given name
     */
    IHyriCosmetic getCosmetic(String name);

    /**
     * Get a cosmetic with his Class
     * @param cosmetic The class of the Cosmetic
     * @return The cosmetic registered with the given class
     */
    IHyriCosmetic getCosmetic(Class<? extends IHyriCosmetic> cosmetic);

    /**
     * Register a cosmetic
     * @param cosmetic The cosmetic to register
     */
    void registerCosmetic(Class<? extends IHyriCosmetic> cosmetic);


    /**
     * Get the cosmetics of a given player
     * @param uuid The player UUID
     * @return A list of owned cosmetics
     */
    List<Class<? extends IHyriCosmetic>> getCosmetics(UUID uuid);

    /**
     * Check if player has a cosmetic
     * @param cosmetic The wanted cosmetic
     * @param uuid The player UUID
     * @return True if player has the given cosmetic
     */
    boolean hasCosmetic(IHyriCosmetic cosmetic, UUID uuid);

    /**
     * Add a cosmetic to a player
     * @param cosmetic The cosmetic to give
     * @param uuid The player to give cosmetic
     */
    void addCosmetic(IHyriCosmetic cosmetic, UUID uuid);

    /**
     * Add multiples cosmetics to a player
     * @param cosmetics List of cosmetics to give
     * @param uuid The player to give cosmetics
     */
    void addCosmetics(List<IHyriCosmetic> cosmetics, UUID uuid);

    /**
     * Remove a cosmetic to a player
     * @param cosmetic The cosmetic to remove
     * @param uuid The player to remove cosmetic
     */
    void removeCosmetic(IHyriCosmetic cosmetic, UUID uuid);

    /**
     * Remove multiples cosmetics to a player
     * @param cosmetics List of cosmetics to remove
     * @param uuid The player to remove cosmetics
     */
    void removeCosmetics(List<IHyriCosmetic> cosmetics, UUID uuid);

    /**
     * Get all registered cosmetics
     * @return The map of all registered cosmetics
     */
    List<Class<? extends IHyriCosmetic>> getRegisteredCosmetics();

    /**
     * Stop a cosmetic for all players
     * @param cosmetic The cosmetic to stop
     */
    void stopForAllPlayers(IHyriCosmetic cosmetic);
}
