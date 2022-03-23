package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.model.HyriMotdChangedEvent;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.event.HyriNetworkEventBus;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 09:16
 */
public class HyriNetwork implements IHyriNetwork {

    private static final String NETWORK_KEY = "network:";
    private static final String MOTD_KEY = NETWORK_KEY + "motd";
    private static final String SLOTS_KEY = NETWORK_KEY + "slots";
    static final String MAINTENANCE_KEY = NETWORK_KEY + "maintenance";

    private final HyriNetworkEventBus eventBus;

    public HyriNetwork() {
        this.eventBus = new HyriNetworkEventBus();
        if (this.getMaintenance() == null) {
            new HyriMaintenance().update();
        }
    }

    @Override
    public HyriNetworkEventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public int getPlayers() {
        return HyriAPI.get().getServerManager().getServers().stream().mapToInt(HyggServer::getPlayers).sum();
    }

    @Override
    public int getSlots() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String slots = jedis.get(SLOTS_KEY);

            if (slots != null) {
                return Integer.parseInt(slots);
            }
            return -1;
        });
    }

    @Override
    public void setSlots(int slots) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(SLOTS_KEY, String.valueOf(slots)));
    }

    @Override
    public String getMotd() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(MOTD_KEY));
    }

    @Override
    public void setMotd(String motd) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(MOTD_KEY, motd));
        HyriAPI.get().getNetwork().getEventBus().publish(new HyriMotdChangedEvent(this.getMotd(), motd));
    }

    @Override
    public IHyriMaintenance getMaintenance() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(MAINTENANCE_KEY);

            if (json != null) {
                return HyriAPI.GSON.fromJson(json, HyriMaintenance.class);
            }
            return null;
        });
    }

}
