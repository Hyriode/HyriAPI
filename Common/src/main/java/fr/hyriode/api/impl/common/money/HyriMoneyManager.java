package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBooster;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.money.IHyriMoneyAction;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.List;
import java.util.UUID;

import static fr.hyriode.api.money.IHyriMoneyAction.*;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 15:54
 */
public class HyriMoneyManager implements IHyriMoneyManager {

    @Override
    public long creditMoney(UUID playerId, IHyriMoneyAction action, IHyriMoney money) {
        final IHyriPlayer player = IHyriPlayer.get(playerId);
        final Type type = action.getType();

        long amount = action.getAmount();
        double multipliers = 1.0D;

        if (action.isMultiplier() && action.getType() == Type.ADD) {
            multipliers = money.getMultiplier(player);

            // Fetching boosters to apply multipliers
            final IHyriBoosterManager boosterManager = HyriAPI.get().getBoosterManager();
            final List<IHyriBooster> boosters = boosterManager.getBoosters(HyriAPI.get().getServer().getType());

            boosters.addAll(boosterManager.getBoosters(IHyriBoosterManager.GLOBAL_TYPE));

            for (IHyriBooster booster : boosters) {
                multipliers *= booster.getMultiplier();
            }

            amount = (long) (amount * multipliers);
        }

        if (amount <= 0) {
            return amount;
        }

        if (type == Type.ADD) {
            money.setAmount(money.getAmount() + amount);
        } else if (type == Type.REMOVE) {
            final long newAmount = money.getAmount() - amount;

            if (newAmount >= 0) {
                money.setAmount(newAmount);
            } else {
                money.setAmount(0);
            }
        }

        if (action.isMessage()) {
            final String message = this.getMoneyMessage(action, amount, multipliers, money);

            HyriAPI.get().getPlayerManager().sendMessage(playerId, message);
        }

        player.update();

        return amount;
    }

    private String getMoneyMessage(IHyriMoneyAction action, long finalAmount, double multipliers, IHyriMoney money) {
        final Type type = action.getType();
        final String reason = action.getReason();
        final String sign = type == Type.ADD ? "+" : "-";
        final boolean withMultipliers = type == Type.ADD && action.isMultiplier() && multipliers > 1.0D;

        return money.getColor() +
                sign +
                finalAmount + " " +
                money.getName() + (withMultipliers ? ("+" + (int) (multipliers * 100)) + "%" + (reason != null ? " ┃ " + reason + ")" : ")") : "") +
                (!withMultipliers && reason != null ? "(" + reason + ")" : "");
    }

}
