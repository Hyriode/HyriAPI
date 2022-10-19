package fr.hyriode.api.impl.common.lootbox;

import fr.hyriode.api.impl.common.transaction.HyriTransaction;
import fr.hyriode.api.lootbox.HyriLootboxRarity;
import fr.hyriode.api.lootbox.HyriLootboxTransaction;
import fr.hyriode.api.lootbox.IHyriLootboxManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.transaction.IHyriTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AstFaster
 * on 19/10/2022 at 15:22
 */
public class HyriLootboxManager implements IHyriLootboxManager {

    @Override
    public void giveLootbox(IHyriPlayer player, HyriLootboxRarity rarity) {
        player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(rarity));
    }

    @Override
    public List<HyriLootboxRarity> getLootboxes(IHyriPlayer player) {
        final List<HyriLootboxRarity> lootboxes = new ArrayList<>();
        final List<? extends IHyriTransaction> transactions = player.getTransactions(HyriLootboxTransaction.TYPE);

        for (IHyriTransaction transaction : transactions) {
            lootboxes.add(transaction.content(HyriLootboxTransaction.class).getLootboxRarity());
        }
        return lootboxes;
    }

}
