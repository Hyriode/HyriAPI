package fr.hyriode.hyriapi.implementation.api.server;

import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.packet.proxy.ProxyMovePlayerPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.server.ServerAskListPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.server.ServerListPacket;
import fr.hyriode.hyriapi.implementation.HyriPlugin;
import fr.hyriode.hyriapi.server.IServerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 20:35
 */
public class ServerManager implements IServerManager {

    private final HyriPlugin plugin;

    public ServerManager(HyriPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getServers() {
        final HyggdrasilChannel returnChannel = HyggdrasilChannel.SERVERS;

        return ((ServerListPacket) (this.plugin.getHyggdrasilManager().sendPacket(HyggdrasilChannel.QUERY, new ServerAskListPacket(returnChannel)).waitResponse(ServerAskListPacket.class, returnChannel))).getServers();
    }

    @Override
    public List<String> getServers(String prefix) {
        final List<String> servers = new ArrayList<>();

        for (String server : this.getServers()) {
            if (server.toLowerCase().startsWith(prefix.toLowerCase())) {
                servers.add(server);
            }
        }

        return servers;
    }

    @Override
    public String getLobbyServer() {
        // TODO Get from redis with Hub Balancer
        return null;
    }

    @Override
    public void sendPlayerToLobby(UUID playerUuid) {
        this.plugin.getHyggdrasilManager().sendPacket(HyggdrasilChannel.QUERY, new ProxyMovePlayerPacket(playerUuid, this.getLobbyServer()));
    }

    @Override
    public void sendPlayerToServer(UUID playerUuid, String server) {
        this.plugin.getHyggdrasilManager().sendPacket(HyggdrasilChannel.QUERY, new ProxyMovePlayerPacket(playerUuid, server));
    }

}
