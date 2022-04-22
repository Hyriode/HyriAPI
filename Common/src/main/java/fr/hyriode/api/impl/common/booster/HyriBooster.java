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
    private final UUID purchaser;
    private final long purchaseDate;

    private long expirationDate;
    private long activatedDate;

    public HyriBooster(String type, double multiplier, UUID purchaser, long purchaseDate) {
        this.identifier = UUID.randomUUID();
        this.type = type;
        this.multiplier = multiplier;
        this.purchaser = purchaser;
        this.purchaseDate = purchaseDate;
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
    public UUID getPurchaser() {
        return this.purchaser;
    }

    @Override
    public Date getPurchaseDate() {
        return new Date(this.purchaseDate);
    }

    @Override
    public Date getActivatedDate() {
        return new Date(this.activatedDate);
    }

    void setActivatedDate(long activatedDate) {
        this.activatedDate = activatedDate;
    }

    @Override
    public Date getExpirationDate() {
        return new Date(this.expirationDate);
    }

    void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean isActive() {
        return this.expirationDate <= this.activatedDate;
    }

}
