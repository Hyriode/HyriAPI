package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.IHyriNetworkManager;
import fr.hyriode.api.network.event.HyriNetworkEventBus;

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
        return HyriAPI.get().getRedisProcessor().get(jedis -> HyriAPI.GSON.fromJson(jedis.get(KEY), HyriNetwork.class));
    }

    @Override
    public void setNetwork(IHyriNetwork network) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY, HyriAPI.GSON.toJson(network)));
    }

}
