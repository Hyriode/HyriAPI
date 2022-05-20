package fr.hyriode.api.impl.common.hyggdrasil;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.hyggdrasil.IHyggdrasilManager;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.listener.HyriProxiesListener;
import fr.hyriode.api.impl.common.hyggdrasil.listener.HyriServersListener;
import fr.hyriode.api.impl.common.redis.HyriRedisConnection;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.HyggdrasilAPI;
import fr.hyriode.hyggdrasil.api.event.model.HyggStartedEvent;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggEnvironment;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggRedisCredentials;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxyState;
import fr.hyriode.hyggdrasil.api.proxy.packet.HyggProxyInfoPacket;
import fr.hyriode.hyggdrasil.api.server.HyggServerOptions;
import fr.hyriode.hyggdrasil.api.server.HyggServerState;
import fr.hyriode.hyggdrasil.api.server.packet.HyggServerInfoPacket;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 18:16
 */
public class HyggdrasilManager implements IHyggdrasilManager {

    private HyggdrasilAPI hyggdrasilAPI;
    private HyggEnvironment environment;

    private final Logger logger;
    private final HyriCommonImplementation implementation;

    public HyggdrasilManager(Logger logger, HyriCommonImplementation implementation) {
        this.logger = logger;
        this.implementation = implementation;

        this.load();
    }

    private void load() {
        if (this.withHyggdrasil()) {
            HyriCommonImplementation.log("Loading Hyggdrasil manager...");

            this.environment = HyggEnvironment.loadFromEnvironmentVariables();
        }
    }

    public void start() {
        if (this.withHyggdrasil()) {
            HyriCommonImplementation.log("Starting Hyggdrasil manager...");

            this.hyggdrasilAPI = new HyggdrasilAPI.Builder()
                    .withLogger(this.logger)
                    .withJedisPool(HyriAPI.get().getRedisConnection().clone().getPool())
                    .withEnvironment(this.environment)
                    .build();
            this.hyggdrasilAPI.start();

            this.hyggdrasilAPI.getScheduler().schedule(this::sendData, 10, 120, TimeUnit.SECONDS);

            this.registerListeners();
        }
    }

    public void sendData() {
        if (this.withHyggdrasil()) {
            final HyggApplication.Type type = this.getApplication().getType();
            final HyggPacketProcessor packetProcessor = this.hyggdrasilAPI.getPacketProcessor();

            if (type == HyggApplication.Type.PROXY) {
                final IHyriProxy proxy = HyriAPI.get().getProxy();

                packetProcessor.request(HyggChannel.PROXIES, new HyggProxyInfoPacket(HyggProxyState.valueOf(proxy.getState().name()), proxy.getPlayers(), proxy.getStartedTime())).exec();
            } else if (type == HyggApplication.Type.SERVER) {
                final IHyriServer server = HyriAPI.get().getServer();

                packetProcessor.request(HyggChannel.SERVERS, new HyggServerInfoPacket(HyggServerState.valueOf(server.getState().name()), server.getPlayers(), server.getPlayersPlaying(), server.getStartedTime(), new HyggServerOptions(), server.getData(), server.getSlots(), server.isAccessible())).exec();
            }
        }
    }

    private void registerListeners() {
        new HyriServersListener(this.implementation).register();
        new HyriProxiesListener(this.implementation).register();

        this.hyggdrasilAPI.getEventBus().subscribe(HyggStartedEvent.class, event -> this.sendData());
    }

    public String generateDevApplicationName() {
        return "dev-" + UUID.randomUUID().toString().substring(0, 5);
    }

    @Override
    public boolean withHyggdrasil() {
        return this.implementation.getConfiguration().withHyggdrasil();
    }

    @Override
    public HyggdrasilAPI getHyggdrasilAPI() {
        return this.hyggdrasilAPI;
    }

    @Override
    public HyggEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public HyggApplication getApplication() {
        return this.environment.getApplication();
    }

}
