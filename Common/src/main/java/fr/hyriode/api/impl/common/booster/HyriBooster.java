package fr.hyriode.api.impl.common.booster;

import fr.hyriode.api.booster.IHyriBooster;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:26
 */
public class HyriBooster implements IHyriBooster {

    private final UUID identifier;
    private final UUID owner;
    private final String game;
    private final double multiplier;
    private final long duration;
    private final long enabledDate;

    public HyriBooster(String game, double multiplier, UUID owner, long duration, long enabledDate) {
        this.enabledDate = enabledDate;
        this.identifier = UUID.randomUUID();
        this.game = game;
        this.multiplier = multiplier;
        this.owner = owner;
        this.duration = duration;
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
