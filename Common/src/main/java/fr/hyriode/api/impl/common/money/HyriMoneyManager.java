package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.money.IHyriMoneyAction;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 15:54
 */
public class HyriMoneyManager implements IHyriMoneyManager {

    @Override
    public void creditMoney(UUID playerId, IHyriMoneyAction action, IHyriMoney money) {
        final IHyriPlayer player = IHyriPlayer.get(playerId);
        final IHyriMoneyAction.Type type = action.getType();
        final long initial = action.getAmount();
        final long amount = type == IHyriMoneyAction.Type.ADD ? (action.isMultiplier() ? money.multiply(initial, player) : initial) : initial;

        if (amount <= 0) {
            return;
        }

        if (type == IHyriMoneyAction.Type.ADD) {
            money.setAmount(money.getAmount() + amount);
        } else if (type == IHyriMoneyAction.Type.REMOVE) {
            final long newAmount = money.getAmount() - amount;

            if (newAmount >= 0) {
                money.setAmount(newAmount);
            } else {
                money.setAmount(0);
            }
        }

        if (action.isMessage()) {
            final String message = this.getMoneyMessage(player, action, amount, money);

            HyriAPI.get().getPlayerManager().sendMessage(playerId, message);
        }

        player.update();
    }

    @Override
    public String getMoneyMessage(IHyriPlayer player, IHyriMoneyAction action, long finalAmount, IHyriMoney money) {
        final IHyriMoneyAction.Type type = action.getType();
        final String reason = action.getReason();

        String sign = "";
        if (type == IHyriMoneyAction.Type.ADD) {
            sign = "+";
        } else if (type == IHyriMoneyAction.Type.REMOVE) {
            sign = "-";
        }

        final int multiplier = (int) money.getMultiplier(player) * 100;

        return money.getColor() + sign + finalAmount + " " + money.getName() + (action.isMultiplier() ? "+" + multiplier + "%" : "") + (reason != null && !reason.isEmpty() ? " (" + reason + ")" : "");
    }

}
