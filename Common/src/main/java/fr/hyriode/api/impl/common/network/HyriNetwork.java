package fr.hyriode.api.impl.common.network;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.impl.common.network.counter.HyriGlobalCounter;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:43
 */
public class HyriNetwork implements IHyriNetwork, DataSerializable {

    @Expose
    private int slots;
    @Expose
    private String motd;
    @Expose
    private final HyriMaintenance maintenance;

    public HyriNetwork() {
        this.slots = -1;
        this.motd = null;
        this.maintenance = new HyriMaintenance();
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeInt(this.slots);
        output.writeString(this.motd);

        this.maintenance.write(output);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.slots = input.readInt();
        this.motd = input.readString();

        this.maintenance.read(input);
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
