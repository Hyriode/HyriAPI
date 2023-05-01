package fr.hyriode.api.money;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 21:55
 */
public interface IHyriMoneyManager {

    String MONEY_REDIS_KEY = "statistics:money:";
    String HYRIS_REDIS_KEY = MONEY_REDIS_KEY + "hyris";
    String HYODES_REDIS_KEY = MONEY_REDIS_KEY + "hyodes";

    /**
     * Apply boosters multipliers on a given amount of money
     *
     * @param playerId The player concerned by the multipliers
     * @param money The type of money
     * @param amount The amount to multiply
     * @return The amount with boosters applied
     */
    long applyBoosters(UUID playerId, IHyriMoney money, long amount);

    /**
     * Get the multipliers for a player
     *
     * @param playerId The player
     * @param money The concerned money
     * @return The multipliers to apply to a money amount
     */
    double getMultipliers(UUID playerId, IHyriMoney money);

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
