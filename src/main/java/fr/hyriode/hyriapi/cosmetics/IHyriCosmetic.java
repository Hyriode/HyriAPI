package fr.hyriode.hyriapi.cosmetics;

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
     * Turn on the cosmetic
     */
    void start();
}
