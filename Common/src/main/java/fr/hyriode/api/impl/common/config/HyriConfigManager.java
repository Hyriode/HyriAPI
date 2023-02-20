package fr.hyriode.api.impl.common.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.IHyriConfig;
import fr.hyriode.api.config.IHyriConfigManager;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AstFaster
 * on 20/02/2023 at 11:05
 */
public class HyriConfigManager implements IHyriConfigManager {

    private final Map<String, MongoCollection<Document>> configsCollections = new HashMap<>();

    @Override
    public <T extends IHyriConfig> T getConfig(Class<T> clazz, String database, String category, String name) {
        final MongoCollection<Document> collection = this.getCollection(database);
        final Document metadata = new Document();

        metadata.append("category", category);
        metadata.append("name", name);

        final Document document = collection.find(Filters.eq("metadata", metadata)).first();

        if (document != null) {
            document.remove("metadata");

            return HyriAPI.GSON.fromJson(document.toJson(), clazz);
        }
        return null;
    }

    @Override
    public <T extends IHyriConfig> T getConfig(Class<T> clazz, String database, String name) {
        return this.getConfig(clazz, database, DEFAULT_CATEGORY, name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void saveConfig(IHyriConfig config, String database, String category, String name) {
        final MongoCollection<Document> collection = this.getCollection(database);
        final Document document = Document.parse(HyriAPI.GSON.toJson(config));
        final Document metadata = new Document();

        metadata.append("category", category);
        metadata.append("name", name);
        document.append("metadata", metadata);

        collection.replaceOne(Filters.eq("metadata", metadata), document, new UpdateOptions().upsert(true));
    }

    @Override
    public void saveConfig(IHyriConfig config, String database, String name) {
        this.saveConfig(config, database, DEFAULT_CATEGORY, name);
    }

    @Override
    public void deleteConfig(String database, String category, String name) {
        final MongoCollection<Document> collection = this.getCollection(database);
        final Document metadata = new Document();

        metadata.append("category", category);
        metadata.append("name", name);

        collection.deleteOne(Filters.eq("metadata", metadata));
    }

    @Override
    public void deleteConfig(String database, String name) {
        this.deleteConfig(database, DEFAULT_CATEGORY, name);
    }

    private MongoCollection<Document> getCollection(String database) {
        MongoCollection<Document> collection = this.configsCollections.get(database);

        if (collection != null) {
            return collection;
        }

        collection = HyriAPI.get().getMongoDB().getDatabase(database).getCollection("configs");

        this.configsCollections.put(database, collection);

        return collection;
    }

}
