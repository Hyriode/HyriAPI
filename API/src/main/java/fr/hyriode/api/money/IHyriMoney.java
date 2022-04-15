package fr.hyriode.api.money;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 21:55
 */
public interface IHyriMoney {

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     * @param reason - Reason to send to player
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     * @param callback - Callback fired after money action done
     */
    void add(long amount, String reason, boolean sendMessage, IHyriMoneyCallback callback);

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     * @param reason - Reason to send to player
     * @param callback - Callback fired after money action done
     */
    void add(long amount, String reason, IHyriMoneyCallback callback);

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     * @param reason - Reason to send to player
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     */
    void add(long amount, String reason, boolean sendMessage);

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     * @param reason - Reason to send to player
     */
    void add(long amount, String reason);

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     * @param callback - Callback fired after money action done
     */
    void add(long amount, boolean sendMessage, IHyriMoneyCallback callback);

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     * @param callback - Callback fired after money action done
     */
    void add(long amount, IHyriMoneyCallback callback);

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     */
    void add(long amount, boolean sendMessage);

    /**
     * Add an amount of money to current player money amount
     *
     * @param amount - Amount to add
     */
    void add(long amount);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     * @param reason - Reason to send to player
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     * @param callback - Callback fired after money action done
     */
    void remove(long amount, String reason, boolean sendMessage, IHyriMoneyCallback callback);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     * @param reason - Reason to send to player
     * @param callback - Callback fired after money action done
     */
    void remove(long amount, String reason, IHyriMoneyCallback callback);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     * @param reason - Reason to send to player
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     */
    void remove(long amount, String reason, boolean sendMessage);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     * @param reason - Reason to send to player
     */
    void remove(long amount, String reason);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     * @param callback - Callback fired after money action done
     */
    void remove(long amount, boolean sendMessage, IHyriMoneyCallback callback);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     * @param callback - Callback fired after money action done
     */
    void remove(long amount, IHyriMoneyCallback callback);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     * @param sendMessage - if <code>true</code> a message will be sent to the player
     */
    void remove(long amount, boolean sendMessage);

    /**
     * Remove an amount of money to current player money amount
     *
     * @param amount - Amount to remove
     */
    void remove(long amount);

    /**
     * Check if the current amount is superior of the given amount
     *
     * @param amount - Checked amount
     * @return - <code>true</code> if the current amount is superior of the given amount
     */
    boolean hasEnough(long amount);

    /**
     * Get current amount of money
     *
     * @return - Amount of money
     */
    long getAmount();

    /**
     * Set amount of money
     *
     * @param amount - Amount of money
     */
    void setAmount(long amount);

    /**
     * Get money display name
     *
     * @return - Display name
     */
    String getName();

    /**
     * Get money color
     *
     * @return A {@link HyriChatColor}
     */
    HyriChatColor getColor();

    enum HyriMoneyAction {
        ADD,
        REMOVE,
    }

}
