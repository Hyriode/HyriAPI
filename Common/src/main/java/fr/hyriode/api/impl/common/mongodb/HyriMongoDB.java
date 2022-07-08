package fr.hyriode.api.impl.common.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.HyriMongoDBConfig;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.mongodb.IHyriMongoDB;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Created by AstFaster
 * on 08/07/2022 at 09:57
 */
public class HyriMongoDB implements IHyriMongoDB {

    private boolean connected;

    private MongoClient client;

    public void startConnection() {
        HyriCommonImplementation.log("Starting MongoDB connection...");

        final HyriMongoDBConfig config = HyriAPI.get().getConfig().getMongoDBConfig();
        final ConnectionString connectionString = new ConnectionString("mongodb://" + config.getUsername() + ":" + config.getPassword() + "@" + config.getHostname() + ":" + config.getPort());
        final CodecRegistry pojoCodecProvider = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecProvider);
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        this.client = MongoClients.create(settings);
        this.connected = true;
    }

    public void stopConnection() {
        HyriCommonImplementation.log("Stopping MongoDB connection...");

        this.client.close();
    }

    @Override
    public MongoDatabase getDatabase(String name) {
        return this.client.getDatabase(name);
    }

    @Override
    public MongoClient getClient() {
        return this.client;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

}
