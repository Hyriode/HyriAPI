package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.network.HyriNetworkCount;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:43
 */
public class HyriNetwork implements IHyriNetwork {

    private final HyriNetworkCount playerCount;
    private int slots;
    private String motd;
    private final HyriMaintenance maintenance;

    public HyriNetwork() {
        this.playerCount = new HyriNetworkCount();
        this.slots = -1;
        this.motd = null;
        this.maintenance = new HyriMaintenance();
    }

    @Override
    public HyriNetworkCount getPlayerCount() {
        return this.playerCount;
    }

    @Override
    public int getSlots() {
        return this.slots;
    }

    @Override
    public void setSlots(int slots) {
        this.slots = slots;
    }

    @Override
    public String getMotd() {
        return this.motd;
    }

    @Override
    public void setMotd(String motd) {
        this.motd = motd;
    }

    @Override
    public IHyriMaintenance getMaintenance() {
        return this.maintenance;
    }

}
