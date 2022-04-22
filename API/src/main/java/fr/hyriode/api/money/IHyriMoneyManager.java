package fr.hyriode.api.money;

import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 21:55
 */
public interface IHyriMoneyManager {

    /**
     * Make action on money when add/remove/set
     *
     * @param player {@link IHyriPlayer}
     * @param action Add/Remove/Set
     * @param money {@link IHyriMoney}
     * @param amount Amount added
     * @param sendMessage If <code>true</code> a message will be sent to the player
     * @param reason Reason to add in message
     * @param callback Callback to fire when money action is done
     */
    void creditMoney(IHyriPlayer player, IHyriMoney.HyriMoneyAction action, IHyriMoney money, long amount, boolean sendMessage, String reason, IHyriMoneyCallback callback);

    /**
     * Get a message to send to player when a reason is given
     *
     * @param action Add/Remove/Set
     * @param money {@link IHyriMoney}
     * @param amount Amount added/removed/set
     * @param reason Reason to add in message
     * @return A formatted message
     */
    String getMoneyMessage(IHyriMoney.HyriMoneyAction action, IHyriMoney money, long amount, String reason);

}
