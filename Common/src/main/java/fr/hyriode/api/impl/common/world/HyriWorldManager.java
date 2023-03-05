package fr.hyriode.api.impl.common.world;

import com.mongodb.client.MongoCursor;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.mongodb.MongoDB;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.world.IHyriWorld;
import fr.hyriode.api.world.IHyriWorldManager;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

/**
 * Created by AstFaster
 * on 19/02/2023 at 18:54
 */
public class HyriWorldManager implements IHyriWorldManager {

    private final Map<String, WorldsFS> storages = new HashMap<>();

    private final MongoDB mongoDB;
    private final WorldCompression compression;

    public HyriWorldManager(MongoDB mongoDB) {
        this.mongoDB = mongoDB;
        this.compression = new WorldCompression();
    }

    @Override
    public @NotNull IHyriWorld.IBuilder newWorld() {
        return new HyriWorld.Builder();
    }

    @Override
    public void saveWorld(@NotNull IHyriWorld world, @NotNull File worldFolder) {
        final byte[] data = this.compression.zipWorld(worldFolder);
        final WorldsFS storage = this.getStorage(world.getDatabase());

        storage.upload((HyriWorld) world, data);

        HyriAPI.get().log("Saved '" + world.getName() + "' world (database: " + world.getDatabase() + "; category: " + world.getCategory() + ").");
    }

    @Override
    public void saveWorld(@NotNull IHyriWorld world, @NotNull UUID worldId) {}

    @Override
    public void updateWorld(@NotNull IHyriWorld in) {
        final HyriWorld world = (HyriWorld) in;
        final WorldsFS storage = this.getStorage(world.getDatabase());
        final Document metadata = MongoSerializer.serialize(world);

        storage.update(world.getInitialCategory(), world.getInitialName(), world.getName(), metadata);

        HyriAPI.get().log("Updated '" + world.getName() + "' world (database: " + world.getDatabase() + "; category: " + world.getCategory() + ").");
    }

    @Override
    public void loadWorld(@NotNull IHyriWorld world, @NotNull File destinationFolder) {
        final WorldsFS storage = this.getStorage(world.getDatabase());
        final byte[] data = storage.download((HyriWorld) world);

        this.compression.unzipWorld(destinationFolder, data);

        HyriAPI.get().log("Loaded '" + world.getName() + "' world (database: " + world.getDatabase() + "; category: " + world.getCategory() + ").");
    }

    @Override
    public void deleteWorld(@NotNull IHyriWorld world) {
        final WorldsFS storage = this.getStorage(world.getDatabase());

        storage.delete((HyriWorld) world);

        HyriAPI.get().log("Deleted '" + world.getName() + "' world (database: " + world.getDatabase() + "; category: " + world.getCategory() + ").");
    }

    @Override
    public void deleteWorld(@NotNull String database, @NotNull String category, @NotNull String name) {
        final IHyriWorld world = this.getWorld(database, category, name);

        if (world != null) {
            this.deleteWorld(world);
        }
    }

    @Override
    public void deleteWorld(@NotNull String database, @NotNull String name) {
        this.deleteWorld(database, DEFAULT_CATEGORY, name);
    }

    @Override
    public IHyriWorld getWorld(@NotNull String database, @NotNull String category, @NotNull String name) {
        final WorldsFS storage = this.getStorage(database);
        final Document document = storage.getDocument(category, name);

        if (document != null) {
            final MongoDocument metadata = MongoDocument.of(document.get("metadata", Document.class));
            final HyriWorld world = new HyriWorld(database, name);

            world.load(metadata);

            return world;
        }
        return null;
    }

    @Override
    public IHyriWorld getWorld(@NotNull String database, @NotNull String name) {
        return this.getWorld(database, DEFAULT_CATEGORY, name);
    }

    @Override
    public @NotNull List<IHyriWorld> getWorlds(@NotNull String database, @NotNull String category) {
        final WorldsFS storage = this.getStorage(database);
        final List<IHyriWorld> worlds = new ArrayList<>();

        try (final MongoCursor<Document> cursor = storage.getDocuments(category)) {
            cursor.forEachRemaining(document -> {
                final MongoDocument metadata = MongoDocument.of(document.get("metadata", Document.class));
                final HyriWorld world = new HyriWorld(database, document.getString("filename"));

                world.load(metadata);

                worlds.add(world);
            });
        }
        return worlds;
    }

    @Override
    public @NotNull List<IHyriWorld> getWorlds(@NotNull String database) {
        final WorldsFS storage = this.getStorage(database);
        final List<IHyriWorld> worlds = new ArrayList<>();

        try (final MongoCursor<Document> cursor = storage.getDocuments()) {
            cursor.forEachRemaining(document -> {
                final MongoDocument metadata = MongoDocument.of(document.get("metadata", Document.class));
                final HyriWorld world = new HyriWorld(database, document.getString("filename"));

                world.load(metadata);

                worlds.add(world);
            });
        }
        return worlds;
    }

    private WorldsFS getStorage(String database) {
        WorldsFS fileStorage = this.storages.get(database);

        if (fileStorage != null) {
            return fileStorage;
        }

        fileStorage = new WorldsFS(this.mongoDB, database);

        this.storages.put(database, fileStorage);

        return fileStorage;
    }

}
