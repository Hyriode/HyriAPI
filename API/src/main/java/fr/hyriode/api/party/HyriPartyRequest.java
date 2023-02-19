package fr.hyriode.api.party;

import fr.hyriode.api.packet.HyriPacket;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 18/02/2023 at 18:48.<br>
 *
 * Represents the request sent by a player to another player to join his party.
 */
public abstract class HyriPartyRequest extends HyriPacket {

    /**
     * Get the id of the party
     *
     * @return The {@link UUID} of the player
     */
    public abstract @NotNull UUID getPartyId();

    /**
     * Get the sender of the request
     *
     * @return A player {@link UUID}
     */
    public abstract @NotNull UUID getSender();

    /**
     * Get the target of the request
     *
     * @return A player {@link UUID}
     */
    public abstract @NotNull UUID getTarget();

}
