package fr.hyriode.api.cosmetic;

public abstract class HyriCosmetic {

    /** The name of the cosmetic */
    protected final String name;
    /** The type of the cosmetic */
    protected final HyriCosmeticType type;
    /** The rarity of the cosmetic */
    protected final HyriCosmeticRarity rarity;

    /**
     * Constructor of {@link HyriCosmetic}
     *
     * @param name The name of the cosmetic
     * @param type The type of the cosmetic
     * @param rarity The rarity of the cosmetic
     */
    public HyriCosmetic(String name, HyriCosmeticType type, HyriCosmeticRarity rarity) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
    }

    /**
     * Get the name of the cosmetic
     *
     * @return Cosmetic name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the type of the cosmetic
     *
     * @return The type of the cosmetic
     */
    public HyriCosmeticType getType() {
        return this.type;
    }

    /**
     * Get the rarity of the cosmetic
     *
     * @return The rarity of the cosmetic
     */
    public HyriCosmeticRarity getRarity() {
        return this.rarity;
    }

}
