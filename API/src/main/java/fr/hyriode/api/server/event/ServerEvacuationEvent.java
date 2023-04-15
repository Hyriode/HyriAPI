package fr.hyriode.api.server.event;

import fr.hyriode.api.event.HyriEvent;

/**
 * Created by AstFaster
 * on 15/04/2023 at 11:16.<br>
 *
 * This event is triggered when a server is evacuated to lobbies.
 */
public class ServerEvacuationEvent extends HyriEvent {

    private final String evacuatedServer;

    public ServerEvacuationEvent(String evacuatedServer) {
        this.evacuatedServer = evacuatedServer;
    }

    public String getEvacuatedServer() {
        return this.evacuatedServer;
    }

}
