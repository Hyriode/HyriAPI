package fr.hyriode.api.cosmetic;

import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/02/2022 at 16:42
 */
public abstract class HyriRunnableCosmetic extends HyriCosmetic {

    /**
     * Constructor of {@link HyriRunnableCosmetic}
     *
     * @param name The name of the cosmetic
     * @param type The type of the cosmetic
     * @param rarity The rarity of the cosmetic
     */
    public HyriRunnableCosmetic(String name, HyriCosmeticType type, HyriCosmeticRarity rarity) {
        super(name, type, rarity);
    }

    /**
     * Start the cosmetic
     *
     * @param player The player to start cosmetic
     */
    public abstract void start( IHyriPlayer player);

    /**
     * Stop the cosmetic
     *
     * @param player The player to stop cosmetic
     */
    public abstract void stop(IHyriPlayer player);

    /**
     * Stop this cosmetic for all players who have activated it
     */
    public abstract void stop();

}
