package fr.hyriode.api.impl.common.lootbox;

import fr.hyriode.api.lootbox.HyriLootboxRarity;
import fr.hyriode.api.lootbox.HyriLootboxTransaction;
import fr.hyriode.api.lootbox.IHyriLootboxManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 19/10/2022 at 15:22
 */
public class HyriLootboxManager implements IHyriLootboxManager {

    @Override
    public void giveLootbox(IHyriPlayer player, HyriLootboxRarity rarity) {
        player.getTransactions().add(HyriLootboxTransaction.TRANSACTIONS_TYPE, new HyriLootboxTransaction(rarity));
        player.update();
    }

    @Override
    public Map<String, HyriLootboxRarity> getLootboxes(IHyriPlayer player) {
        final Map<String, HyriLootboxRarity> lootboxes = new HashMap<>();
        final List<IHyriTransaction> transactions = player.getTransactions().getAll(HyriLootboxTransaction.TRANSACTIONS_TYPE);

        if (transactions == null) {
            return lootboxes;
        }

        for (IHyriTransaction transaction : transactions) {
            lootboxes.put(transaction.name(), transaction.loadContent(new HyriLootboxTransaction()).getRarity());
        }
        return lootboxes;
    }

}
