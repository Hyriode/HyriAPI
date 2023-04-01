package fr.hyriode.api.host;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

import java.util.Optional;

/**
 * Created by AstFaster
 * on 01/04/2023 at 12:49.<br>
 *
 * This event is triggered each time the advertisement for a host is made.
 */
public class HostAdvertisementEvent extends HyriEvent {

    private final String serverName;

    public HostAdvertisementEvent(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return this.serverName;
    }

    public Optional<HostData> getHostData() {
        final HyggServer server = HyriAPI.get().getServerManager().getServer(this.serverName);

        return server == null ? Optional.empty() : Optional.ofNullable(HyriAPI.get().getHostManager().getHostData(server));
    }

}
