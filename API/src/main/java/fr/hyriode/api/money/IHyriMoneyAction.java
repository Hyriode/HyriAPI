package fr.hyriode.api.money;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/05/2022 at 07:19
 */
public interface IHyriMoneyAction {

    /**
     * All the available types of actions
     */
    enum Type {

        /** The action of addition */
        ADD,
        /** The action of removing */
        REMOVE

    }

    /**
     * Set the amount of the action
     *
     * @param amount The new amount of money
     * @return This {@link IHyriMoneyAction} instance
     */
    IHyriMoneyAction withAmount(long amount);

    /**
     * Get the amount of the action
     *
     * @return An amount
     */
    long getAmount();

    /**
     * Set the reason of the action
     *
     * @param reason The new reason of the action
     * @return This {@link IHyriMoneyAction} instance
     */
    IHyriMoneyAction withReason(String reason);

    /**
     * Get the reason of the action
     *
     * @return A message
     */
    String getReason();

    /**
     * Set the if the money multiplier will be used
     *
     * @param multiplier <code>true</code> if the player is used
     * @return This {@link IHyriMoneyAction} instance
     */
    IHyriMoneyAction withMultiplier(boolean multiplier);

    /**
     * Check if the multiplier will be used
     *
     * @return <code>true</code> if yes
     */
    boolean isMultiplier();

    /**
     * Set the message to send after the action
     *
     * @param message The new message to send
     * @return This {@link IHyriMoneyAction} instance
     */
    IHyriMoneyAction withMessage(boolean message);

    /**
     * Check if a message will be used
     *
     * @return <code>true</code> if yes
     */
    boolean isMessage();

    /**
     * Get the type of the action
     *
     * @return A {@link Type}
     */
    Type getType();

    /**
     * Execute the money action
     *
     * @return The amount of money removed or added (with multipliers)
     */
    long exec();

}
