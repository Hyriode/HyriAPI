package fr.hyriode.api.impl.common.player.model;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.player.model.IHyriTransaction;
import fr.hyriode.api.player.model.IHyriTransactionContent;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by AstFaster
 * on 20/06/2022 at 17:56
 */
public class HyriTransaction implements IHyriTransaction, MongoSerializable, DataSerializable {

    @Expose
    private String name;
    @Expose
    private long timestamp;
    @Expose
    private Document content;

    // Cached content
    private IHyriTransactionContent contentCache;

    public HyriTransaction() {}

    public HyriTransaction(String name, long timestamp, IHyriTransactionContent content) {
        this.name = name;
        this.timestamp = timestamp;
        this.contentCache = content;
    }

    @Override
    public void save(MongoDocument document) {
        if (this.contentCache != null) {
            this.content = MongoSerializer.serialize(this.contentCache);
        }

        document.append("name", this.name);
        document.append("timestamp", this.timestamp);
        document.append("content", this.content);
    }

    @Override
    public void load(MongoDocument document) {
        this.name = document.getString("name");
        this.timestamp = document.getLong("timestamp");
        this.content = document.get("content", Document.class);
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        if (this.contentCache != null) {
            this.content = MongoSerializer.serialize(this.contentCache);
        }

        output.writeString(this.name);
        output.writeLong(this.timestamp);
        output.writeByteArray(MongoSerializer.serialize(this.content));
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.name = input.readString();
        this.timestamp = input.readLong();
        this.content = MongoSerializer.deserialize(input.readByteArray());
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public long timestamp() {
        return this.timestamp;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IHyriTransactionContent> T content() {
        return this.contentCache != null ? (T) this.contentCache : null;
    }

    @Override
    public <T extends IHyriTransactionContent> T loadContent(@NotNull T emptyContent) {
        emptyContent.load(MongoDocument.of(this.content));

        this.contentCache = emptyContent;

        return emptyContent;
    }

}
