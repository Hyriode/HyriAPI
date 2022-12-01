package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.money.IHyriMoneyAction;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 01/12/2022 at 19:57
 */
public class Gems extends HyriMoney {

    public Gems(UUID playerUUID) {
        super(playerUUID);
    }

    @Override
    public IHyriMoneyAction add(long amount) {
        return new HyriMoneyAction(IHyriMoneyAction.Type.ADD, this.playerUUID, amount, this).withMultiplier(false);
    }

    @Override
    public IHyriMoneyAction remove(long amount) {
        return new HyriMoneyAction(IHyriMoneyAction.Type.REMOVE, this.playerUUID, amount, this).withMultiplier(false);
    }

    @Override
    public String getName() {
        return "Gems";
    }

    @Override
    public HyriChatColor getColor() {
        return HyriChatColor.GREEN;
    }

}
