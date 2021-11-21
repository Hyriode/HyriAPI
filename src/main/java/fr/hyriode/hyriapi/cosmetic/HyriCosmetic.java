package fr.hyriode.hyriapi.cosmetic;

import fr.hyriode.hyriapi.player.IHyriPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class HyriCosmetic {

    private final String name;
    private final HyriCosmeticType type;
    private final HyriCosmeticRarity rarity;

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

    /**
     * Start the cosmetic
     *
     * @param player The player to start cosmetic
     */
    public abstract void start( IHyriPlayer player);

    /**
     * Stop the cosmetic
     *
     * @param plugin Spigot plugin
     * @param player The player to stop cosmetic
     */
    public abstract void stop(JavaPlugin plugin, IHyriPlayer player);

    /**
     * Stop this cosmetic for all players who have activated it
     */
    public abstract void stop();

}
