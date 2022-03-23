package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.money.IHyriMoneyCallback;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 15:54
 */
public abstract class HyriCommonMoneyManager implements IHyriMoneyManager {

    @Override
    public void creditMoney(IHyriPlayer player, IHyriMoney.HyriMoneyAction action, IHyriMoney money, long amount, boolean sendMessage, String reason, IHyriMoneyCallback callback) {
        if (action.equals(IHyriMoney.HyriMoneyAction.ADD)) {
            money.setAmount(money.getAmount() + amount);
        } else if (action.equals(IHyriMoney.HyriMoneyAction.REMOVE)) {
            final long newAmount = money.getAmount() - amount;

            if (newAmount >= 0) {
                money.setAmount(newAmount);
            } else {
                throw new IllegalArgumentException("Cannot set player money under 0!");
            }
        } else {
            throw new IllegalArgumentException("Money action is not valid!");
        }

        if (sendMessage) {
            final String message = this.getMoneyMessage(action, money, amount, reason);

            HyriAPI.get().getPlayerManager().sendMessage(player.getUniqueId(), message);
        }

        if (callback != null) {
            callback.call(action, reason, money.getAmount(), amount);
        }

        HyriAPI.get().getPlayerManager().sendPlayer(player);
    }

}
