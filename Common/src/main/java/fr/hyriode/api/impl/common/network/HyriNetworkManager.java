package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.IHyriNetworkManager;
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

    public HyriNetworkManager() {
        this.eventBus = new HyriNetworkEventBus();

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
    public void setNetwork(IHyriNetwork network) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY.getBytes(StandardCharsets.UTF_8), HyriAPI.get().getDataSerializer().serialize((HyriNetwork) network)));
    }

}
