package fr.hyriode.api.impl.common.host;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.host.IHostManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by AstFaster
 * on 19/11/2022 at 12:31
 */
public class HostManager implements IHostManager {

    @Override
    public @Nullable HostData getHostData(HyggServer server) {
        return server.getData().getObject(DATA_KEY, HostData.class);
    }

    @Override
    public Set<HyggServer> getHosts() {
        final Set<HyggServer> servers = new HashSet<>();

        for (HyggServer server : HyriAPI.get().getServerManager().getServers()) {
            if (server.getAccessibility() == HyggServer.Accessibility.HOST) {
                servers.add(server);
            }
        }
        return servers;
    }

}
