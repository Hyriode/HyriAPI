package fr.hyriode.api.impl.common.booster;

import fr.hyriode.api.booster.IHyriBooster;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:26
 */
public class HyriBooster implements IHyriBooster {

    private final UUID identifier;
    private final String type;
    private final double multiplier;
    private final UUID owner;
    private final long duration;

    public HyriBooster(String type, double multiplier, UUID owner, long duration) {
        this.identifier = UUID.randomUUID();
        this.type = type;
        this.multiplier = multiplier;
        this.owner = owner;
        this.duration = duration;
    }

    @Override
    public UUID getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getType() {
        return this.type;
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

}
