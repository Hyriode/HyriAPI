package fr.hyriode.api.player.model;

import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 18/02/2023 at 09:43.<br>
 *
 * Represents the requests sent by {@linkplain IHyriPlayer players} to get new {@linkplain IHyriFriend friends}.
 */
public abstract class HyriFriendRequest extends HyriPacket {

    /**
     * Get the sender of the request
     *
     * @return The {@link UUID} of a player
     */
    public abstract UUID getSender();

    /**
     * Get the target of the request
     *
     * @return The {@link UUID} of a player
     */
    public abstract UUID getTarget();

}
