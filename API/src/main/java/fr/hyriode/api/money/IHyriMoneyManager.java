package fr.hyriode.api.money;

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
     * @return The added/removed money
     */
    long creditMoney(UUID playerId, IHyriMoneyAction action, IHyriMoney money);

}
