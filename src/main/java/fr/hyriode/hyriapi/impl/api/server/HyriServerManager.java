package fr.hyriode.hyriapi.impl.api.server;

import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.packet.proxy.ProxyMovePlayerPacket;
import fr.hyriode.hyriapi.impl.HyriPlugin;
import fr.hyriode.hyriapi.impl.thread.ThreadPool;
import fr.hyriode.hyriapi.server.IHyriServerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 20:35
 */
public class HyriServerManager implements IHyriServerManager {

    private List<String> servers;

    private final HyriPlugin plugin;

    public HyriServerManager(HyriPlugin plugin) {
        this.plugin = plugin;
        this.servers = new ArrayList<>();

        new HyriServerPacketHandler(this.plugin);

        ThreadPool.EXECUTOR.scheduleWithFixedDelay(new HyriServerPacketTask(this.plugin), 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public List<String> getServers() {
        return this.servers;
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

    protected void setServers(List<String> servers) {
        this.servers = servers;
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
