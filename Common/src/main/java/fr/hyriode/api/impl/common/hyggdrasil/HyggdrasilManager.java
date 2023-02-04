package fr.hyriode.api.impl.common.hyggdrasil;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.host.IHostManager;
import fr.hyriode.api.hyggdrasil.IHyggdrasilManager;
import fr.hyriode.api.impl.common.hyggdrasil.listener.LimbosListener;
import fr.hyriode.api.impl.common.hyggdrasil.listener.ProxiesListener;
import fr.hyriode.api.impl.common.hyggdrasil.listener.ServersListener;
import fr.hyriode.api.limbo.IHyriLimbo;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.HyggdrasilAPI;
import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;
import fr.hyriode.hyggdrasil.api.limbo.packet.HyggLimboInfoPacket;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggEnv;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import fr.hyriode.hyggdrasil.api.proxy.packet.HyggProxyInfoPacket;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.packet.HyggServerInfoPacket;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 18:16
 */
public class HyggdrasilManager implements IHyggdrasilManager {

    private HyggdrasilAPI hyggdrasilAPI;
    private HyggEnv environment;

    public HyggdrasilManager(HyggEnv environment) {
        this.environment = environment;

        if (this.environment == null) {
            this.load();
        }
    }

    private void load() {
        if (this.withHyggdrasil()) {
            HyriAPI.get().log("Loading Hyggdrasil manager...");

            this.environment = HyggEnv.loadFromEnvironmentVariables();
        }
    }

    public void start(Logger logger) {
        if (this.withHyggdrasil()) {
            HyriAPI.get().log("Starting Hyggdrasil manager...");

            this.hyggdrasilAPI = new HyggdrasilAPI.Builder()
                    .withJedisPool(HyriAPI.get().getRedisConnection().getPool())
                    .withEnvironment(this.environment)
                    .withLogger(logger)
                    .build();
            this.hyggdrasilAPI.start();

            HyriAPI.get().getScheduler().schedule(this::sendData, 10, 120, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        if (!this.withHyggdrasil()) {
            return;
        }

        HyriAPI.get().log("Stopping Hyggdrasil manager...");

        this.hyggdrasilAPI.stop();
    }

    @Override
    public void sendData() {
        if (this.withHyggdrasil()) {
            final HyggApplication.Type type = this.getApplication().getType();
            final HyggPacketProcessor packetProcessor = this.hyggdrasilAPI.getPacketProcessor();

            if (type == HyggApplication.Type.PROXY) {
                final IHyriProxy proxy = HyriAPI.get().getProxy();

                packetProcessor.request(HyggChannel.PROXIES, new HyggProxyInfoPacket(new HyggProxy(proxy.getName(), proxy.getData(), proxy.getState(), proxy.getPlayers(), proxy.getStartedTime()))).exec();
            } else if (type == HyggApplication.Type.SERVER) {
                final IHyriServer server = HyriAPI.get().getServer();
                final HostData hostData = server.getHostData();

                if (hostData != null) {
                    server.getData().addObject(IHostManager.DATA_KEY, hostData);
                }

                final HyggServer info = new HyggServer(
                        server.getName(), server.getType(), server.getGameType(),
                        server.getMap(), server.getAccessibility(), server.getProcess(),
                        server.getState(), server.getData(), server.getPlayers(),
                        server.getPlayersPlaying(), server.getSlots(), server.getStartedTime());

                packetProcessor.request(HyggChannel.SERVERS, new HyggServerInfoPacket(info)).exec();
            } else if (type == HyggApplication.Type.LIMBO) {
                final IHyriLimbo limbo = HyriAPI.get().getLimbo();

                final HyggLimbo info = new HyggLimbo(limbo.getName(), limbo.getData(), limbo.getType(), limbo.getState(), limbo.getPlayers(), limbo.getStartedTime());

                packetProcessor.request(HyggChannel.LIMBOS, new HyggLimboInfoPacket(info)).exec();
            }
        }
    }

    public void registerListeners() {
        if (this.withHyggdrasil()) {
            new ServersListener().register();
            new ProxiesListener().register();
            new LimbosListener().register();
        }
    }

    @Override
    public boolean withHyggdrasil() {
        return HyriAPI.get().getConfig().withHyggdrasil();
    }

    @Override
    public HyggdrasilAPI getHyggdrasilAPI() {
        return this.hyggdrasilAPI;
    }

    @Override
    public HyggEnv getEnvironment() {
        return this.environment;
    }

    @Override
    public HyggApplication getApplication() {
        return this.environment.getApplication();
    }

}
