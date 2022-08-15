package fr.hyriode.api.player.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/06/2022 at 19:37
 */
public class PlayerJoinServerEvent extends HyriEvent {

    /** The unique id of the player that joined a server */
    private final UUID playerId;
    /** The name of the server */
    private final String serverName;

    /**
     * The constructor of {@link  PlayerJoinServerEvent}
     *
     * @param playerId The player's id
     * @param serverName The name of the server
     */
    public PlayerJoinServerEvent(UUID playerId, String serverName) {
        this.playerId = playerId;
        this.serverName = serverName;
    }

    /**
     * Get the unique id of the player that joined a server
     *
     * @return The unique id of the player; cannot be <code>null</code>
     */
    public UUID getPlayerId() {
        return this.playerId;
    }

    /**
     * Get the account of the player that joined a server
     *
     * @return The {@linkplain  IHyriPlayer player's account}
     */
    public IHyriPlayer getPlayer() {
        return IHyriPlayer.get(this.playerId);
    }

    /**
     * Get the name of the server
     *
     * @return The server name
     */
    public String getServerName() {
        return this.serverName;
    }

}
