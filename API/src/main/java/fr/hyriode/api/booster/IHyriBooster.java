package fr.hyriode.api.booster;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 19:49
 */
public interface IHyriBooster {

    /**
     * An {@link UUID} that represents the booster
     *
     * @return An {@link UUID}
     */
    UUID getIdentifier();

    /**
     * Get the type of the booster.<br>
     * It can be a game name or ALL
     *
     * @return A type
     */
    String getType();

    /**
     * Get the multiplier of the booster.<br>
     * It can be 1.5, 2.5 etc
     *
     * @return A multiplier
     */
    double getMultiplier();

    /**
     * Get the unique id of the purchaser of the booster
     *
     * @return A player {@link UUID}
     */
    UUID getPurchaser();

    /**
     * Get the date when the booster has been bought
     *
     * @return A {@link Date}
     */
    Date getPurchaseDate();

    /**
     * Get the date when the booster has been activated
     *
     * @return A {@link Date}
     */
    Date getActivatedDate();

    /**
     * Get the expiration date of a booster
     *
     * @return A {@link Date}
     */
    Date getExpirationDate();

    /**
     * Check if the booster is currently active
     *
     * @return <code>true</code> if the booster is active
     */
    boolean isActive();

}
