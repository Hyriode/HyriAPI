package fr.hyriode.api.player.model;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 18/02/2023 at 09:34.<br>
 *
 * Represents what a friend of {@linkplain IHyriPlayer player} is.
 */
public interface IHyriFriend {

    /**
     * Get the unique id of the friend
     *
     * @return A player {@link UUID}
     */
    UUID getUniqueId();

    /**
     * Get the timestamp when the friend was added
     *
     * @return A timestamp (in milliseconds)
     */
    long whenAdded();

}
