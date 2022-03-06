package fr.hyriode.api.impl.server.money;

import fr.hyriode.api.impl.common.money.HyriCommonMoneyManager;
import fr.hyriode.api.money.IHyriMoney;
import org.bukkit.ChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:45
 */
public class HyriMoneyManager extends HyriCommonMoneyManager {

    @Override
    public String getMoneyMessage(IHyriMoney.HyriMoneyAction action, IHyriMoney money, long amount, String reason) {
        String sign = "";
        if (action.equals(IHyriMoney.HyriMoneyAction.ADD)) {
            sign = "+";
        } else if (action.equals(IHyriMoney.HyriMoneyAction.REMOVE)) {
            sign = "-";
        }

        final ChatColor color = ChatColor.getByChar(money.getColorChar());

        return color + sign + amount + " " + money.getName() + (reason != null && !reason.isEmpty() ? " (" + reason + ")" : "");
    }

}
