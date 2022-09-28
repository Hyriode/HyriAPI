package fr.hyriode.api.rank.hyriplus;

import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.transaction.IHyriTransactionContent;

import java.util.Date;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 11:31
 */
public class HyriPlus {

    /** The priority of the Hyri+ in queues */
    public static final int PRIORITY = HyriPlayerRankType.EPIC.getPriority() + 1;
    /** The priority of the Hyri+ in the tab list */
    public static final int TAB_LIST_PRIORITY = HyriPlayerRankType.EPIC.getTabListPriority() + 1;

    /** The duration of the Hyri+ in milliseconds */
    private long duration;
    /** The timestamp when the Hyri+ has been enabled */
    private long enabledDate = -1;

    /**
     * Enabled the Hyri+
     */
    public void enable() {
        this.enabledDate = System.currentTimeMillis();
    }

    /**
     * Get the duration of the Hyri+
     *
     * @return A time in milliseconds
     */
    public long getDuration() {
        return this.duration;
    }

    /**
     * Set the duration of the Hyri+
     *
     * @param duration The new duration of the Hyri+
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Get the date when the Hyri+ has been enabled
     *
     * @return A {@link Date} or <code>null</code> if the Hyri+ is not enabled
     */
    public Date getEnabledDate() {
        return this.enabledDate == -1 ? null : new Date(this.enabledDate);
    }

    /**
     * Check if the Hyri+ offer has expired
     *
     * @return <code>true</code> if the expiration date is passed
     */
    public boolean hasExpire() {
        final boolean result = System.currentTimeMillis() >= this.enabledDate + this.duration;

        if (result) {
            this.enabledDate = -1;
            this.duration = 0;
        }

        return result;
    }

}
