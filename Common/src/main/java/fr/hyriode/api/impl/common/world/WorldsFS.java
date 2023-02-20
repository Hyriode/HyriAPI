package fr.hyriode.api.impl.common.world;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.mongodb.MongoSerializer;
import org.bson.Document;

/**
 * Created by AstFaster
 * on 19/02/2023 at 19:06
 */
public class WorldsFS {

    private final GridFSBucket gridFSBucket;
    private final MongoCollection<Document> filesCollection;
    private final String database;

    public WorldsFS(String database) {
        this.database = database;
        this.gridFSBucket = GridFSBuckets.create(HyriAPI.get().getMongoDB().getDatabase(this.database), "worlds");
        this.filesCollection = HyriAPI.get().getMongoDB().getDatabase(this.database).getCollection("worlds.files");
    }

    public void upload(HyriWorld world, byte[] data) {
        final GridFSFile file = this.getFile(world);
        final boolean exists = file != null;

        if (exists) {
            this.gridFSBucket.delete(file.getObjectId());
        }

        final GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(4096 * 4096 - 64)
                .metadata(MongoSerializer.serialize(world));

        try (final GridFSUploadStream uploadStream = this.gridFSBucket.openUploadStream(world.getName(), options)) {
            uploadStream.write(data);
            uploadStream.flush();
        }
    }

    public void delete(HyriWorld world) {
        final GridFSFile file = this.getFile(world);

        if (file != null) {
            this.gridFSBucket.delete(file.getObjectId());

            HyriAPI.get().log("Deleted '" + world.getName() + "' world (database: " + this.database + "; category: " + world.getCategory() + ").");
        }
    }

    public byte[] download(HyriWorld world) {
        final GridFSFile file = this.getFile(world);

        if (file == null) {
            return null;
        }

        try (final GridFSDownloadStream downloadStream = this.gridFSBucket.openDownloadStream(file.getObjectId())) {
            final int length = (int) downloadStream.getGridFSFile().getLength();
            final byte[] data = new byte[length];

            downloadStream.read(data);

            return data;
        }
    }

    public void updateMetadata(String category, String name, Document metadata) {
        this.filesCollection.updateOne(Filters.and(Filters.eq("metadata.category", category), Filters.eq("filename", name)), Updates.combine(Updates.set("metadata", metadata), Updates.set("filename", name)));
    }

    public GridFSFile getFile(HyriWorld world) {
        return this.gridFSBucket.find(Filters.and(Filters.eq("metadata.category", world.getCategory()), Filters.eq("filename", world.getName()))).first();
    }

    public Document getDocument(String category, String name) {
        return this.filesCollection.find(Filters.and(Filters.eq("metadata.category", category), Filters.eq("filename", name))).first();
    }

    public MongoCursor<Document> getDocuments(String category) {
        return this.filesCollection.find(Filters.eq("metadata.category", category)).iterator();
    }

    public MongoCursor<Document> getDocuments() {
        return this.filesCollection.find().iterator();
    }

}