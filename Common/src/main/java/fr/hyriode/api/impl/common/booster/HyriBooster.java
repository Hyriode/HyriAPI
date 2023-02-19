package fr.hyriode.api.impl.common.booster;

import fr.hyriode.api.booster.IHyriBooster;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:26
 */
public class HyriBooster implements IHyriBooster, DataSerializable {

    private UUID identifier;
    private UUID owner;
    private String game;
    private double multiplier;
    private long duration;
    private long enabledDate;

    public HyriBooster() {}

    public HyriBooster(String game, double multiplier, UUID owner, long duration, long enabledDate) {
        this.identifier = UUID.randomUUID();
        this.game = game;
        this.multiplier = multiplier;
        this.owner = owner;
        this.duration = duration;
        this.enabledDate = enabledDate;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.identifier);
        output.writeUUID(this.owner);
        output.writeString(this.game);
        output.writeDouble(this.multiplier);
        output.writeLong(this.duration);
        output.writeLong(this.enabledDate);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.identifier = input.readUUID();
        this.owner = input.readUUID();
        this.game = input.readString();
        this.multiplier = input.readDouble();
        this.duration = input.readLong();
        this.enabledDate = input.readLong();
    }

    @Override
    public UUID getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getGame() {
        return this.game;
    }

    @Override
    public double getMultiplier() {
        return this.multiplier;
    }

    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public long getEnabledDate() {
        return this.enabledDate;
    }

    @Override
    public long getDisabledDate() {
        return this.enabledDate + this.duration * 1000;
    }

}
