package fr.hyriode.api.rank;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.transaction.IHyriTransactionContent;

import java.util.Date;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 11:31
 */
public class HyriPlus implements IHyriTransactionContent {

    /** The type of Hyri+ transaction */
    public static final String TRANSACTION_TYPE = "hyri+";

    /** The priority of the Hyri+ in queues */
    public static final int PRIORITY = HyriPlayerRankType.EPIC.getPriority() + 1;
    /** The priority of the Hyri+ in the tab list */
    public static final int TAB_LIST_PRIORITY = HyriPlayerRankType.EPIC.getTabListPriority() + 1;

    /** The timestamp of when the Hyri+ has been bought */
    private final long purchaseDate;
    /** The timestamp of when the Hyri+ will expire */
    private final long expirationDate;
    /** The color of the + symbol */
    private HyriChatColor plusColor = HyriChatColor.LIGHT_PURPLE;

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
     * Get the color of the + symbol
     *
     * @return A {@link HyriChatColor}
     */
    public HyriChatColor getPlusColor() {
        return this.plusColor;
    }

    /**
     * Set the color of the + symbol
     *
     * @param plusColor A {@link HyriChatColor}
     */
    public void setPlusColor(HyriChatColor plusColor) {
        this.plusColor = plusColor;
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
