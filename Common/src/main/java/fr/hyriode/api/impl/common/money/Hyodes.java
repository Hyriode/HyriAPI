package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.money.IHyriMoneyAction;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Created by AstFaster
 * on 01/12/2022 at 19:57
 */
public class Hyodes extends HyriMoney {

    public Hyodes(IHyriPlayer player) {
        super(player);
    }

    @Override
    public IHyriMoneyAction add(long amount) {
        return new HyriMoneyAction(IHyriMoneyAction.Type.ADD, this.player.getUniqueId(), amount, this).withMultiplier(false);
    }

    @Override
    public IHyriMoneyAction remove(long amount) {
        return new HyriMoneyAction(IHyriMoneyAction.Type.REMOVE, this.player.getUniqueId(), amount, this).withMultiplier(false);
    }

    @Override
    public String getName() {
        return "Hyodes";
    }

    @Override
    public HyriChatColor getColor() {
        return HyriChatColor.GREEN;
    }

}
