package fr.hyriode.api.rank.hyriplus;

import fr.hyriode.api.transaction.IHyriTransactionContent;

/**
 * Created by AstFaster
 * on 21/09/2022 at 17:50
 */
public class HyriPlusTransaction implements IHyriTransactionContent {

    /** The type of Hyri+ transaction */
    public static final String TRANSACTION_TYPE = "hyri+";

    /** The duration of the Hyri+ */
    private final long duration;

    /**
     * Constructor of a {@link HyriPlusTransaction}
     *
     * @param duration The duration of the Hyri+
     */
    public HyriPlusTransaction(long duration) {
        this.duration = duration;
    }

    /**
     * Get the duration of the Hyri+
     *
     * @return A duration in milliseconds
     */
    public long getDuration() {
        return this.duration;
    }

}
