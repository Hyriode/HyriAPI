package fr.hyriode.api.friend;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:13
 */
public interface IHyriFriend {

    /**
     * Get the unique id of the friend
     *
     * @return An {@link UUID}
     */
    UUID getUniqueId();

    /**
     * Get the date when the player has become a friend
     *
     * @return A {@link Date}
     */
    Date getWhenAdded();

}
