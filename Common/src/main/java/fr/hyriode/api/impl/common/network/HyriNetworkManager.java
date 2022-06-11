package fr.hyriode.api.impl.common.network;

import com.google.gson.JsonElement;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.IHyriNetworkManager;
import fr.hyriode.api.network.event.HyriNetworkEventBus;
import fr.hyriode.hydrion.client.module.NetworkModule;

import java.util.concurrent.ExecutionException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 09:16
 */
public class HyriNetworkManager implements IHyriNetworkManager {

    private static final String KEY = "network";

    private final HyriNetworkEventBus eventBus;

    private final HydrionManager hydrionManager;
    private NetworkModule networkModule;

    public HyriNetworkManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;
        this.eventBus = new HyriNetworkEventBus();

        if (this.hydrionManager.isEnabled()) {
            this.networkModule = this.hydrionManager.getClient().getNetworkModule();
        }

        IHyriNetwork network = this.getNetwork();

        if (network == null) {
            network = new HyriNetwork();
        }

        this.setNetwork(network);
    }

    public void cacheNetwork(IHyriNetwork network) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY, HyriAPI.GSON.toJson(network)));
    }

    @Override
    public HyriNetworkEventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public IHyriNetwork getNetwork() {
        final IHyriNetwork network = HyriAPI.get().getRedisProcessor().get(jedis -> this.deserialize(jedis.get(KEY)));

        if (network != null) {
            return network;
        }

        if (this.hydrionManager.isEnabled() && HyriAPI.get().getConfiguration().isProduction()) {
            try {
                return this.networkModule.getNetwork().thenApply(response -> {
                    final JsonElement content = response.getContent();

                    if (!content.isJsonNull()) {
                        return this.deserialize(response.getContent().toString());
                    }
                    return null;
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private HyriNetwork deserialize(String json) {
        return HyriAPI.GSON.fromJson(json, HyriNetwork.class);
    }

    @Override
    public void setNetwork(IHyriNetwork network) {
        this.cacheNetwork(network);

        if (this.hydrionManager.isEnabled() && HyriAPI.get().getConfiguration().isProduction()) {
            this.networkModule.setNetwork(HyriAPI.GSON.toJson(network));
        }
    }

}
