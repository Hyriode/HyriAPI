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
    private final NetworkModule networkModule;

    public HyriNetworkManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;
        this.networkModule = this.hydrionManager.getClient().getNetworkModule();
        this.eventBus = new HyriNetworkEventBus();

        if (this.getNetwork() == null) {
            this.setNetwork(new HyriNetwork());
        }
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

        if (this.hydrionManager.isEnabled()) {
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
        final String serialized = HyriAPI.GSON.toJson(network);

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(KEY, serialized));

        if (this.hydrionManager.isEnabled()) {
            this.networkModule.setNetwork(serialized);
        }
    }

}
