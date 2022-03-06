package fr.hyriode.api.money;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 21:35
 */
@FunctionalInterface
public interface IHyriMoneyCallback {

    /**
     * Call after an economy action is done
     *
     * @param action - Action done on money
     * @param reason - Reason of economy action
     * @param newAmount - The new amount of the money
     * @param difference - The difference between before and after
     */
    void call(IHyriMoney.HyriMoneyAction action, String reason, long newAmount, long difference);

}
