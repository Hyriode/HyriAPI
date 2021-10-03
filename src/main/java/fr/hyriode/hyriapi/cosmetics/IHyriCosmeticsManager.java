package fr.hyriode.hyriapi.cosmetics;

import fr.hyriode.hyriapi.player.IHyriPlayer;

public interface IHyriCosmeticsManager {

    boolean hasCosmetic(IHyriPlayer player, IHyriCosmetic cosmetic);

    void registerCosmetic(IHyriCosmetic cosmetic);
}
