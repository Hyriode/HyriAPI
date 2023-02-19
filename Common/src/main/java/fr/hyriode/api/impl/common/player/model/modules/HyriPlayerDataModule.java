package fr.hyriode.api.impl.common.player.model.modules;

import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.player.model.IHyriPlayerData;
import fr.hyriode.api.player.model.modules.IHyriPlayerDataModule;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by AstFaster
 * on 17/02/2023 at 12:00.<br>
 *
 * Extends {@link HashMap} for better JSON serialization.
 */
public class HyriPlayerDataModule extends HashMap<String, Document> implements IHyriPlayerDataModule, DataSerializable, MongoSerializable {

    // Cached data (deserialized during process)
    private final Map<String, IHyriPlayerData> data = new HashMap<>();

    private void synchronizeData() {
        for (Map.Entry<String, IHyriPlayerData> entry : this.data.entrySet()) {
            this.put(entry.getKey(), MongoSerializer.serialize(entry.getValue()));
        }
    }

    @Override
    public void save(MongoDocument document) {
        this.synchronizeData();

        document.append("data", this);
    }

    @Override
    public void load(MongoDocument document) {
        final Document data = document.get("data", Document.class);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            final Document dataDocument = (Document) entry.getValue();

            this.put(entry.getKey(), dataDocument);
        }
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        this.synchronizeData();

        output.writeInt(this.size());

        for (Map.Entry<String, Document> entry : this.entrySet()) {
            output.writeString(entry.getKey());
            output.writeByteArray(MongoSerializer.serialize(entry.getValue()));
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            this.put(input.readString(), MongoSerializer.deserialize(input.readByteArray()));
        }
    }

    @Override
    public @NotNull Set<String> keys() {
        return this.keySet();
    }

    @Override
    public void add(@NotNull String key, @NotNull IHyriPlayerData data) {
        this.data.put(key, data);
    }

    @Override
    public void remove(@NotNull String key) {
        this.data.remove(key);
        super.remove(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IHyriPlayerData> T get(@NotNull String key) {
        return (T) this.data.get(key);
    }

    @Override
    public <T extends IHyriPlayerData> T read(@NotNull String key, @NotNull T emptyData) {
        final Document dataDocument = super.get(key);

        if (dataDocument == null) {
            return null;
        }

        emptyData.load(MongoDocument.of(dataDocument));

        this.data.put(key, emptyData);

        return emptyData;
    }

    @Override
    public boolean has(@NotNull String key) {
        return this.containsKey(key);
    }

}
