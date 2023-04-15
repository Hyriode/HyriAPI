package fr.hyriode.api.server.event;

import fr.hyriode.api.event.HyriEvent;

/**
 * Created by AstFaster
 * on 15/04/2023 at 11:20.<br>
 *
 * This event is triggered when a lobby will restart (maybe to deploy an update).
 */
public class LobbyRestartingEvent extends HyriEvent {

    /** The name of the lobby that will restart */
    private final String lobby;
    /** The count before the lobbies restart */
    private final int count;

    public LobbyRestartingEvent(String lobby, int count) {
        this.lobby = lobby;
        this.count = count;
    }

    public String getLobby() {
        return this.lobby;
    }

    public int getCount() {
        return this.count;
    }

}
