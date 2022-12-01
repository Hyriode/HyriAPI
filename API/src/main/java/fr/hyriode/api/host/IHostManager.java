package fr.hyriode.api.host;

import fr.hyriode.hyggdrasil.api.server.HyggServer;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Created by AstFaster
 * on 16/11/2022 at 18:00
 */
public interface IHostManager {

    /** The channel used by the host communication system */
    String CHANNEL = "hosts";
    /** The key of the {@link HostData} object in {@link HyggServer} */
    String DATA_KEY = "host";

    /**
     * Get the {@link HostData} of a given server
     *
     * @param server The server
     * @return The {@link HostData} of the server; or <code>null</code> if the server is not running a host
     */
    @Nullable
    HostData getHostData(HyggServer server);

    /**
     * Get the running hosts on the server.
     *
     * @return A list of {@link HyggServer}
     */
    Set<HyggServer> getHosts();

}
