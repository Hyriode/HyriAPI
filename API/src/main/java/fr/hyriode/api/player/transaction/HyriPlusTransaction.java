package fr.hyriode.api.player.transaction;

import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.player.model.IHyriTransactionContent;

/**
 * Created by AstFaster
 * on 21/09/2022 at 17:50
 */
public class HyriPlusTransaction implements IHyriTransactionContent {

    /** The type of Hyri+ transactions */
    public static final String TRANSACTIONS_TYPE = "hyri+";

    /** The duration of the Hyri+ */
    private long duration;

    public HyriPlusTransaction() {}

    /**
     * Constructor of a {@link HyriPlusTransaction}
     *
     * @param duration The duration of the Hyri+
     */
    public HyriPlusTransaction(long duration) {
        this.duration = duration;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("duration", this.duration);
    }

    @Override
    public void load(MongoDocument document) {
        this.duration = document.getLong("duration");
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
