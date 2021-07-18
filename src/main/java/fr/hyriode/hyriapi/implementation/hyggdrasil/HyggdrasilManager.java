package fr.hyriode.hyriapi.implementation.hyggdrasil;

import com.google.gson.Gson;
import fr.hyriode.hyggdrasilconnector.HyggdrasilConnector;
import fr.hyriode.hyggdrasilconnector.api.ServerState;
import fr.hyriode.hyggdrasilconnector.protocol.HyggdrasilConnectionManager;
import fr.hyriode.hyggdrasilconnector.protocol.channel.HyggdrasilChannel;
import fr.hyriode.hyggdrasilconnector.protocol.packet.HyggdrasilPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.common.HeartbeatPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.server.ServerAskInfoPacket;
import fr.hyriode.hyggdrasilconnector.protocol.packet.server.ServerInfoPacket;
import fr.hyriode.hyriapi.implementation.HyriPlugin;
import fr.hyriode.hyriapi.implementation.api.server.HyriServer;
import fr.hyriode.hyriapi.implementation.configuration.nested.RedisConfiguration;
import fr.hyriode.hyriapi.implementation.hyggdrasil.listener.HyggdrasilClientPacketListener;
import fr.hyriode.hyriapi.implementation.hyggdrasil.task.HeartbeatTask;
import fr.hyriode.hyriapi.implementation.thread.ThreadPool;
import fr.hyriode.hyriapi.server.Server;
import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:49
 */
public class HyggdrasilManager {

    private long lastClientHeartbeat;
    private boolean connected;

    private ScheduledFuture<?> heartbeatTask;

    private String clientId;
    private Server server;

    private final HyggdrasilConnector hyggdrasilConnector;

    private final HyriPlugin plugin;

    public HyggdrasilManager(HyriPlugin plugin) {
        this.plugin = plugin;

        final RedisConfiguration configuration = this.plugin.getConfiguration().getRedisConfiguration();

        this.hyggdrasilConnector = new HyggdrasilConnector(new Gson(), configuration.getRedisIp(), configuration.getRedisPort(), configuration.getRedisPassword());
        this.connected = false;
    }

    public void start() {
        this.hyggdrasilConnector.start();

        this.getHyggdrasilEnvs();

        this.hyggdrasilConnector.getEventManager().registerListener(new HyggdrasilClientPacketListener(this));

        this.heartbeatTask = ThreadPool.EXECUTOR.scheduleWithFixedDelay(new HeartbeatTask(this), 2, 20, TimeUnit.SECONDS);
    }

    public void stop() {
        this.heartbeatTask.cancel(true);

        this.hyggdrasilConnector.stop();
    }

    private void getHyggdrasilEnvs() {
        this.clientId = System.getenv("CLIENT_ID");

        String serverId = null;
        try {
            serverId = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        final long startedTime = Long.parseLong(System.getenv("STARTED_TIME"));
        final int slots = Integer.parseInt(System.getenv("SLOTS"));
        final ServerState serverState = ServerState.fromInteger(Integer.parseInt(System.getenv("STATE")));

        this.server = new HyriServer(serverId, startedTime, slots, Bukkit.getOnlinePlayers().size(), serverState);
    }

    public void heartbeat(HeartbeatPacket packet) {
        if (packet.getApplicationType().equals(HeartbeatPacket.ApplicationType.HYGGDRASIL_CLIENT)) {
            this.lastClientHeartbeat = System.currentTimeMillis();

            if (!this.connected) {
                this.connected = true;

                this.plugin.log("Server is now connected with his Hyggdrasil Client (" + packet.getApplicationId() + ").");
            }
        }
    }

    public void askInfo(ServerAskInfoPacket packet) {
        if (packet.getServerId().equals(this.server.getId())) {
            this.sendInfo(packet.getReturnChannel());
        }
    }

    public void sendInfo(HyggdrasilChannel channel) {
        final ServerInfoPacket outPacket = new ServerInfoPacket(this.server.getId(), Bukkit.getOnlinePlayers().size(), this.server.getSlots(), this.server.getState(), this.server.getStartedTime());

        this.sendPacket(channel, outPacket);
    }

    public void sendPacket(String channel, HyggdrasilPacket packet) {
        this.getConnectionManager().sendPacket(channel, packet);
    }

    public void sendPacket(HyggdrasilChannel channel, HyggdrasilPacket packet) {
        this.getConnectionManager().sendPacket(channel, packet);
    }

    public HyggdrasilConnectionManager getConnectionManager() {
        return this.hyggdrasilConnector.getConnectionManager();
    }

    public HyggdrasilConnector getHyggdrasilConnector() {
        return this.hyggdrasilConnector;
    }

    public Server getServer() {
        return this.server;
    }

    public String getClientId() {
        return this.clientId;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public long getLastClientHeartbeat() {
        return this.lastClientHeartbeat;
    }

}