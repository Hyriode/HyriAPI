package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.network.counter.HyriGlobalCounter;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.IHyriNetworkManager;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.api.network.event.HyriNetworkEventBus;

import java.nio.charset.StandardCharsets;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 09:16
 */
public class HyriNetworkManager implements IHyriNetworkManager {

    private static final String KEY = "network";

    private final HyriNetworkEventBus eventBus;
    private final HyriGlobalCounter globalCounter;

    public HyriNetworkManager() {
        this.eventBus = new HyriNetworkEventBus();
        this.globalCounter = new HyriGlobalCounter();

        IHyriNetwork network = this.getNetwork();

        if (network == null) {
            network = new HyriNetwork();
        }

        this.setNetwork(network);
    }

    @Override
    public HyriNetworkEventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public IHyriNetwork getNetwork() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final byte[] bytes = jedis.get(KEY.getBytes(StandardCharsets.UTF_8));

            return bytes == null ? null : HyriAPI.get().getDataSerializer().deserialize(new HyriNetwork(), bytes);
        });
    }

    @Override
    public IHyriGlobalCounter getPlayerCounter() {
        return this.globalCounter;
    }

    @Override
    public void setNetwork(IHyriNetwork network) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(KEY.getBytes(StandardCharsets.UTF_8), HyriAPI.get().getDataSerializer().serialize((HyriNetwork) network)));
    }

}
