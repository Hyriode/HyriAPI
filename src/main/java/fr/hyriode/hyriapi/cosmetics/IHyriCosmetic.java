package fr.hyriode.hyriapi.cosmetics;

import fr.hyriode.hyriapi.player.IHyriPlayer;

public interface IHyriCosmetic {

    /**
     * Get the name of the cosmetic
     * @return Cosmetic name
     */
    String getName();

    /**
     * Get the type of the cosmetic
     * @return The type of the cosmetic
     */
    HyriCosmeticType getType();

    /**
     * Get the rarity of the cosmetic
     * @return The rarity of the cosmetic
     */
    HyriCosmeticRarity getRarity();

    /**
     * Start the cosmetic
     * @param player The player to start cosmetic
     */
    void start(IHyriPlayer player);

    /**
     * Stop the cosmetic
     * @param player The player to stop cosmetic
     */
    void stop(IHyriPlayer player);

    /**
     * Stop this cosmetic for all players who have activated it
     */
    void stop();
}
