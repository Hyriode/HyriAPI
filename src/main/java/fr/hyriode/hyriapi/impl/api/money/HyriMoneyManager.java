package fr.hyriode.hyriapi.impl.api.money;

import fr.hyriode.hyriapi.impl.HyriPlugin;
import fr.hyriode.hyriapi.impl.thread.ThreadPool;
import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.money.IHyriMoneyCallback;
import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 22:03
 */
public class HyriMoneyManager implements IHyriMoneyManager {

    private final HyriPlugin plugin;

    public HyriMoneyManager(HyriPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void creditMoney(IHyriPlayer player, IHyriMoney.HyriMoneyAction action, IHyriMoney money, long amount, String reason, IHyriMoneyCallback callback) {
        ThreadPool.EXECUTOR.execute(() -> {
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

            final String message = this.getMoneyMessage(action, money, amount, reason);

            Bukkit.getPlayer(player.getUUID()).sendMessage(message);

            if (callback != null) {
                callback.call(action, money.getAmount(), amount);
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

        return color + sign + " " + amount + " " + money.getName() + (reason != null && !reason.isEmpty() ? " : " + reason : "");
    }

}
