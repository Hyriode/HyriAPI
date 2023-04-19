package fr.hyriode.api.booster;

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
     * Get the type of the booster
     *
     * @return A {@link Type}
     */
    Type getType();

    /**
     * Get the unique id of the booster's owner
     *
     * @return A player {@link UUID}
     */
    UUID getOwner();

    /**
     * Get the game boosted by the booster.
     *
     * @return A type
     */
    String getGame();

    /**
     * Get the multiplier of the booster.<br>
     * It can be 1.5, 2.5 etc
     *
     * @return A multiplier
     */
    double getMultiplier();

    /**
     * Get the duration of the booster
     *
     * @return A duration in seconds
     */
    long getDuration();

    /**
     * Check whether the booster is enabled or not
     *
     * @return <code>true</code> if it is enabled
     */
    boolean isEnabled();

    /**
     * Get the time when the booster has been enabled (or will be enabled).
     *
     * @return A timestamp (in milliseconds)
     */
    long getEnabledDate();

    /**
     * Get the time when the booster will be disabled.
     *
     * @return A timestamp (in milliseconds)
     */
    long getDisabledDate();

    /**
     * The different types of boosters running on the network.
     */
    enum Type {

        /** This booster is combined to other boosters. It bypasses the queue system. */
        COMBINED,
        /** This booster is a normal booster. It uses the queue system */
        NORMAL

    }

}
