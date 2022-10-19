package fr.hyriode.api.lootbox;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.List;

/**
 * Created by AstFaster
 * on 19/10/2022 at 15:21
 */
public interface IHyriLootboxManager {

    /**
     * Give a lootbox to a player
     *
     * @param player The player
     * @param rarity The rarity of the lootbox to give
     */
    void giveLootbox(IHyriPlayer player, HyriLootboxRarity rarity);

    /**
     * Get all the owned lootboxes of a player
     *
     * @param player The player
     * @return A list of {@link HyriLootboxRarity}
     */
    List<HyriLootboxRarity> getLootboxes(IHyriPlayer player);

}
