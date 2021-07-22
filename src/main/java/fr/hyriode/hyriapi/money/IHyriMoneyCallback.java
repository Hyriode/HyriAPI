package fr.hyriode.hyriapi.money;

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
     * @param newAmount - The new amount of the money
     * @param difference - The difference between before and after
     */
    void call(IHyriMoney.HyriMoneyAction action, long newAmount, long difference);

}
