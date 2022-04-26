package fr.hyriode.api.impl.proxy.server;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.server.HyriCServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/04/2022 at 16:53
 */
public class HyriServerManager extends HyriCServerManager {

    public HyriServerManager(HyriCommonImplementation implementation) {
        super(implementation);
    }

    @Override
    public void sendPlayerToServer(UUID playerUUID, String serverName) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);

        if (player != null) {
            super.sendPlayerToServer(playerUUID, serverName);
        }
    }

    @Override
    public void sendPartyToServer(UUID partyId, String serverName) {
        super.sendPartyToServer(partyId, serverName);
    }

}
