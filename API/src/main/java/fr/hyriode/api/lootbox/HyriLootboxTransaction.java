package fr.hyriode.api.lootbox;

import fr.hyriode.api.transaction.IHyriTransactionContent;

/**
 * Created by AstFaster
 * on 07/10/2022 at 20:56
 */
public class HyriLootboxTransaction implements IHyriTransactionContent {

    public static final String TYPE = "lootbox";

    private final HyriLootboxRarity lootboxRarity;

    public HyriLootboxTransaction(HyriLootboxRarity lootboxRarity) {
        this.lootboxRarity = lootboxRarity;
    }

    public HyriLootboxRarity getLootboxRarity() {
        return this.lootboxRarity;
    }

}
