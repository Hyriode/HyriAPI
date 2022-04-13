package fr.hyriode.api.rank;

import java.util.Date;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 11:31
 */
public class HyriPlus {

    /** The timestamp of when the Hyri+ has been bought */
    private final long purchaseDate;
    /** The timestamp of when the Hyri+ will expire */
    private final long expirationDate;

    /**
     * Constructor of {@link HyriPlus}
     *
     * @param purchaseDate The purchase date
     * @param expirationDate The expiration date
     */
    public HyriPlus(long purchaseDate, long expirationDate) {
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
    }

    /**
     * Get the date when the Hyri+ has been purchased
     *
     * @return A {@link Date}
     */
    public Date getPurchaseDate() {
        return new Date(this.purchaseDate);
    }

    /**
     * Get the date the Hyri+ offer will expire
     *
     * @return A {@link Date}
     */
    public Date getExpirationDate() {
        return new Date(this.expirationDate);
    }

    /**
     * Check if the Hyri+ offer has expired
     *
     * @return <code>true</code> if the expiration date is passed
     */
    public boolean hasExpire() {
        return this.expirationDate <= System.currentTimeMillis();
    }

}
