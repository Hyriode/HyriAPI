package fr.hyriode.api.booster;

import fr.hyriode.api.transaction.IHyriTransactionContent;

/**
 * Created by AstFaster
 * on 07/10/2022 at 21:02
 */
public class HyriBoosterTransaction implements IHyriTransactionContent {

    /** The type of boosters' transactions */
    public static final String TRANSACTIONS_TYPE = "boosters";

    /** The type of the booster */
    private final String type;
    /** The multiplier of the booster */
    private final double multiplier;
    /** The duration of the booster */
    private final long duration;
    /** Define whether the booster of the transaction has been used or not */
    private boolean used;

    public HyriBoosterTransaction(String type, double multiplier, long duration) {
        this.type = type;
        this.multiplier = multiplier;
        this.duration = duration;
    }

    public String getType() {
        return this.type;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public long getDuration() {
        return this.duration;
    }

    public boolean isUsed() {
        return this.used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

}
