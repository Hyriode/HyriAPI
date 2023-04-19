package fr.hyriode.api.lootbox;

import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.player.model.IHyriTransactionContent;

/**
 * Created by AstFaster
 * on 07/10/2022 at 20:56
 */
public class HyriLootboxTransaction implements IHyriTransactionContent {

    public static final String TRANSACTIONS_TYPE = "lootboxes";

    private HyriLootboxRarity rarity;

    public HyriLootboxTransaction() {}

    public HyriLootboxTransaction(HyriLootboxRarity rarity) {
        this.rarity = rarity;
    }

    public HyriLootboxRarity getRarity() {
        return this.rarity;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("rarity", this.rarity.name());
    }

    @Override
    public void load(MongoDocument document) {
        this.rarity = HyriLootboxRarity.valueOf(document.getString("rarity"));
    }

}
