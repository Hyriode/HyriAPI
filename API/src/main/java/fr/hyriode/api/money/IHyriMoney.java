package fr.hyriode.api.money;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 21:55
 */
public interface IHyriMoney {

    /**
     * Create an addition action
     *
     * @param amount The default amount to add
     * @return A {@link IHyriMoneyAction} instance
     */
    IHyriMoneyAction add(long amount);

    /**
     * Create a removing action
     *
     * @param amount The default amount to remove
     * @return A {@link IHyriMoneyAction} instance
     */
    IHyriMoneyAction remove(long amount);

    /**
     * Check if the current amount is superior of the given amount
     *
     * @param amount Checked amount
     * @return <code>true</code> if the current amount is superior of the given amount
     */
    boolean hasEnough(long amount);

    /**
     * Get current amount of money
     *
     * @return Amount of money
     */
    long getAmount();

    /**
     * Set amount of money
     *
     * @param amount Amount of money
     */
    void setAmount(long amount);

    /**
     * Get money display name
     *
     * @return Display name
     */
    String getName();

    /**
     * Get money color
     *
     * @return A {@link HyriChatColor}
     */
    HyriChatColor getColor();

    /**
     * Get the money multiplier for a given account
     *
     * @param account The account of the player
     * @return The multiplier to use for the account
     */
    default double getMultiplier(IHyriPlayer account) {
        return 1.0D;
    }

}
