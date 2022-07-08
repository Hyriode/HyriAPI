package fr.hyriode.api.impl.common.network;

import fr.hyriode.api.impl.common.network.counter.HyriGlobalCounter;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:43
 */
public class HyriNetwork implements IHyriNetwork {

    private int slots;
    private String motd;
    private final HyriMaintenance maintenance;

    public HyriNetwork() {
        this.slots = -1;
        this.motd = null;
        this.maintenance = new HyriMaintenance();
    }


    @Override
    public IHyriGlobalCounter getPlayerCounter() {
        return new HyriGlobalCounter();
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
