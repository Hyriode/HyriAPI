package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.HyriAPIPlugin;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.packet.model.HyriSendPlayerPacket;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.request.HyggRequestHeader;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.protocol.response.IHyggResponse;
import fr.hyriode.hyggdrasil.api.proxy.packet.HyggEvacuatePacket;
import fr.hyriode.hyggdrasil.api.proxy.packet.HyggProxyServerActionPacket;
import fr.hyriode.hyggdrasil.api.proxy.packet.HyggStopProxyPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 22:13
 */
public class HyriProxyReceiver implements IHyggPacketReceiver, IHyriPacketReceiver {

    @Override
    public IHyggResponse receive(String channel, HyggPacket packet, HyggRequestHeader header) {
        if (packet instanceof HyggProxyServerActionPacket) {
            final long before = System.currentTimeMillis();
            final HyggProxyServerActionPacket serverAction = (HyggProxyServerActionPacket) packet;
            final HyggProxyServerActionPacket.Action action = serverAction.getAction();
            final String serverName = serverAction.getServerName();
            final int serverPort = serverAction.getServerPort();

            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
            if (serverInfo != null && action == HyggProxyServerActionPacket.Action.REMOVE) {
                ProxyServer.getInstance().getServers().remove(serverName);

                HyriAPIPlugin.log("Removed '" + serverName + "' server (time: " + (System.currentTimeMillis() - before) + ").");
            } else if (serverInfo == null && action == HyggProxyServerActionPacket.Action.ADD) {
                serverInfo = ProxyServer.getInstance().constructServerInfo(serverName, new InetSocketAddress(serverName, serverPort), "", false);

                ProxyServer.getInstance().getServers().put(serverName, serverInfo);

                HyriAPIPlugin.log("Added '" + serverName + "' server (time: " + (System.currentTimeMillis() - before) + ").");
            }
            return HyggResponse.Type.SUCCESS;
        } else if (packet instanceof HyggStopProxyPacket) {
            final String proxyName = ((HyggStopProxyPacket) packet).getProxyName();
            final IHyriProxy proxy = HyriAPI.get().getProxy();

            if (proxy.getName().equals(proxyName)) {
                proxy.getStopHandler().run();

                return HyggResponse.Type.SUCCESS;
            }
            return HyggResponse.Type.NONE;
        } else if (packet instanceof HyggEvacuatePacket) {
            final HyggEvacuatePacket evacuatePacket = (HyggEvacuatePacket) packet;

            this.evacuateServer(evacuatePacket.getFrom(), evacuatePacket.getTo());
        }
        return HyggResponse.Type.NONE;
    }

    private void evacuateServer(String fromName, String toName) {
        final ServerInfo from = ProxyServer.getInstance().getServerInfo(fromName);

        if (from == null) {
            return;
        }

        for (ProxiedPlayer player : from.getPlayers()) {
            this.connectPlayer(player.getUniqueId(), toName);
        }
    }

    private void connectPlayer(UUID playerId, String server) {
        final ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server);
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerId);

        if (player != null && player.isConnected() && serverInfo != null) {
            player.connect(serverInfo);
        }
    }

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof HyriSendPlayerPacket) {
            final HyriSendPlayerPacket sendPacket = (HyriSendPlayerPacket) packet;

            this.connectPlayer(sendPacket.getPlayerUUID(), sendPacket.getServerName());
        }
    }

}
