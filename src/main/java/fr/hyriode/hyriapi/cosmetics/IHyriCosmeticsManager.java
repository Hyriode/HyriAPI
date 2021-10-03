package fr.hyriode.hyriapi.cosmetics;

import fr.hyriode.hyriapi.player.IHyriPlayer;

import java.util.List;
import java.util.UUID;

public interface IHyriCosmeticsManager {

    boolean hasCosmetics(IHyriPlayer player);
    List<IHyriCosmetic> getCosmetics(UUID uuid);
    List<IHyriCosmetic> getCosmetics(IHyriPlayer player);

    void giveCosmetic(IHyriPlayer player, IHyriCosmetic cosmetic);
    void giveCosmetics(IHyriPlayer player, List<IHyriCosmetic> cosmetics);

    void registerCosmetic(IHyriCosmetic cosmetic);
}
