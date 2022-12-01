package fr.hyriode.api.impl.common.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.MongoDBConfig;
import fr.hyriode.api.mongodb.IMongoDB;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by AstFaster
 * on 08/07/2022 at 09:57
 */
public class HyriMongoDB implements IMongoDB {

    private boolean connected;

    private MongoClient client;

    public void startConnection() {
        HyriAPI.get().log("Starting MongoDB connection...");

        final MongoDBConfig config = HyriAPI.get().getConfig().getMongoDBConfig();
        final ConnectionString connectionString = new ConnectionString(config.toURL());
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.client").setLevel(Level.OFF);

        this.client = MongoClients.create(settings);
        this.connected = true;
    }

    public void stopConnection() {
        HyriAPI.get().log("Stopping MongoDB connection...");

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
