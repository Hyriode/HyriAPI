package fr.hyriode.api.impl.common.player.model.modules;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.player.model.IHyriStatistics;
import fr.hyriode.api.player.model.modules.IHyriStatisticsModule;
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
 * on 17/02/2023 at 13:35
 */
public class HyriStatisticsModule implements IHyriStatisticsModule, MongoSerializable, DataSerializable {

    @Expose
    private Map<String, Long> playTime = new HashMap<>();
    @Expose
    private Map<String, Document> games = new HashMap<>();

    // Cached games statistics
    private final Map<String, IHyriStatistics> gamesCache = new HashMap<>();

    private long totalPlayTime;

    private void synchronizeData() {
        for (Map.Entry<String, IHyriStatistics> entry : this.gamesCache.entrySet()) {
            this.games.put(entry.getKey(), MongoSerializer.serialize(entry.getValue()));
        }
    }

    @Override
    public void save(MongoDocument document) {
        this.synchronizeData();

        document.append("play_time", this.playTime);
        document.append("games", this.games);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load(MongoDocument document) {
        this.playTime = document.get("play_time", this.playTime.getClass());
        this.games = document.get("games", this.games.getClass());
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        this.synchronizeData();

        output.writeInt(this.playTime.size());

        for (Map.Entry<String, Long> entry : this.playTime.entrySet()) {
            output.writeString(entry.getKey());
            output.writeLong(entry.getValue());
        }

        output.writeInt(this.games.size());

        for (Map.Entry<String, Document> entry : this.games.entrySet()) {
            output.writeString(entry.getKey());
            output.writeByteArray(MongoSerializer.serialize(entry.getValue()));
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        final int playTimeSize = input.readInt();

        for (int i = 0; i < playTimeSize; i++) {
            this.playTime.put(input.readString(), input.readLong());
        }

        final int gamesSize = input.readInt();

        for (int i = 0; i < gamesSize; i++) {
            this.games.put(input.readString(), MongoSerializer.deserialize(input.readByteArray()));
        }
    }

    @Override
    public long getTotalPlayTime() {
        return this.totalPlayTime;
    }

    @Override
    public long getPlayTime(@NotNull String category) {
        return this.playTime.getOrDefault(category, 0L);
    }

    @Override
    public void addPlayTime(@NotNull String category, long playTime) {
        this.totalPlayTime += playTime;

        this.playTime.merge(category, playTime, Long::sum);
    }

    @Override
    public @NotNull Set<String> keys() {
        return this.games.keySet();
    }

    @Override
    public void add(@NotNull String key, @NotNull IHyriStatistics statistics) {
        this.gamesCache.put(key, statistics);
    }

    @Override
    public void remove(@NotNull String key) {
        this.gamesCache.remove(key);
        this.games.remove(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IHyriStatistics> T get(@NotNull String key) {
        return (T) this.gamesCache.get(key);
    }

    @Override
    public <T extends IHyriStatistics> T read(@NotNull String key, @NotNull T emptyData) {
        final Document statisticsDocument = this.games.get(key);

        if (statisticsDocument == null) {
            return null;
        }

        emptyData.load(MongoDocument.of(statisticsDocument));

        this.gamesCache.put(key, emptyData);

        return emptyData;
    }

    @Override
    public boolean has(@NotNull String key) {
        return this.games.containsKey(key);
    }

}
