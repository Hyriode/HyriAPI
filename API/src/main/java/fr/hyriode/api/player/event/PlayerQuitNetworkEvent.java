package fr.hyriode.api.player.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/06/2022 at 19:37
 */
public class PlayerQuitNetworkEvent extends HyriEvent {

    /** The unique id of the player that quit the network */
    private final UUID playerId;

    /**
     * The constructor of {@link  PlayerQuitNetworkEvent}
     *
     * @param playerId The player's id
     */
    public PlayerQuitNetworkEvent(UUID playerId) {
        this.playerId = playerId;
    }

    /**
     * Get the unique id of the player that quit the network
     *
     * @return The unique id of the player; cannot be <code>null</code>
     */
    public UUID getPlayerId() {
        return this.playerId;
    }

    /**
     * Get the account of the player that quit the network
     *
     * @return The {@linkplain  IHyriPlayer player's account}
     */
    public IHyriPlayer getPlayer() {
        return IHyriPlayer.get(this.playerId);
    }

}
