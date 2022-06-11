package fr.hyriode.api.money;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 21:55
 */
public interface IHyriMoneyManager {

    /**
     * Make action on money when add/remove/set
     *
     * @param playerId The unique id of the player
     * @param action The action that will be done on the money
     * @param money The {@link IHyriMoney} that will be affected
     */
    void creditMoney(UUID playerId, IHyriMoneyAction action, IHyriMoney money);

    /**
     * Get a message to send to player when a reason is given
     *
     * @param player The player that will receive the message
     * @param action The action done on the money
     * @param money {@link IHyriMoney}
     * @param finalAmount The final amount added/removed
     * @return A formatted message
     */
    String getMoneyMessage(IHyriPlayer player, IHyriMoneyAction action, long finalAmount, IHyriMoney money);

}
