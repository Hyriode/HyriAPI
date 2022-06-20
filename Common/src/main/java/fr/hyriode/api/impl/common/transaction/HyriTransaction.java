package fr.hyriode.api.impl.common.transaction;

import com.google.gson.JsonElement;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.transaction.IHyriTransaction;
import fr.hyriode.api.transaction.IHyriTransactionContent;

/**
 * Created by AstFaster
 * on 20/06/2022 at 17:56
 */
public class HyriTransaction implements IHyriTransaction {

    private final String name;
    private final long timestamp;
    private final JsonElement content;

    public HyriTransaction(String name, long timestamp, IHyriTransactionContent content) {
        this.name = name;
        this.timestamp = timestamp;
        this.content = HyriAPI.GSON.toJsonTree(content);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public long timestamp() {
        return this.timestamp;
    }

    @Override
    public <T extends IHyriTransactionContent> T content(Class<T> contentClass) {
        if (content == null || contentClass == null) {
            return null;
        }
        return HyriAPI.GSON.fromJson(content, contentClass);
    }

}
