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
     * Get the unique id of the booster's owner
     *
     * @return A player {@link UUID}
     */
    UUID getOwner();

    /**
     * Get the duration of the booster
     *
     * @return A duration in seconds
     */
    long getDuration();

}
