package fr.hyriode.hyriapi.impl.money;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.money.IHyriMoneyCallback;
import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import org.bukkit.ChatColor;

import java.util.concurrent.Executors;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriMoneyManager implements IHyriMoneyManager {

    private final HyriAPIPlugin plugin;

    public HyriMoneyManager(HyriAPIPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void creditMoney(IHyriPlayer player, IHyriMoney.HyriMoneyAction action, IHyriMoney money, long amount, boolean sendMessage, String reason, IHyriMoneyCallback callback) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
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

            player.setMoney(money);

            if (sendMessage) {
                final String message = this.getMoneyMessage(action, money, amount, reason);

                HyriAPI.get().getPlayerManager().sendMessage(player.getUUID(), message);
            }

            if (callback != null) {
                callback.call(action, reason, money.getAmount(), amount);
            }

            this.plugin.getAPI().getPlayerManager().sendPlayer(player);
        });
    }

    @Override
    public String getMoneyMessage(IHyriMoney.HyriMoneyAction action, IHyriMoney money, long amount, String reason) {
        String sign = "";
        if (action.equals(IHyriMoney.HyriMoneyAction.ADD)) {
            sign = "+";
        } else if (action.equals(IHyriMoney.HyriMoneyAction.REMOVE)) {
            sign = "-";
        }

        final ChatColor color = ChatColor.getByChar(money.getColorChar());

        return color + sign + amount + " " + money.getName() + (reason != null && !reason.isEmpty() ? " : " + reason : "");
    }

}
