package fr.hyriode.api.impl.common.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.MongoDBConfig;
import fr.hyriode.api.mongodb.IMongoDB;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by AstFaster
 * on 08/07/2022 at 09:57
 */
public class MongoDB implements IMongoDB {

    private boolean connected;

    private MongoClient client;

    private final MongoDBConfig config;

    public MongoDB(MongoDBConfig config) {
        this.config = config;
    }

    public void startConnection() {
        HyriAPI.get().log("Starting MongoDB connection...");

        final ConnectionString connectionString = new ConnectionString(this.config.toURL());
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();

        this.client = MongoClients.create(settings);
        this.connected = true;

//        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
//        Logger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
//        Logger.getLogger("org.mongodb.driver.client").setLevel(Level.OFF);
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
