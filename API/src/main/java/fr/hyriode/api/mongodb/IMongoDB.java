package fr.hyriode.api.mongodb;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;

/**
 * Created by AstFaster
 * on 07/07/2022 at 20:09
 */
public interface IMongoDB {

    /**
     * Get a MongoDB database by its name
     *
     * @param name The name of the database to get
     * @return The database with the given name
     */
    MongoDatabase getDatabase(String name);

    /**
     * Get the MongoDB client
     *
     * @return The {@link MongoClient} instance
     */
    MongoClient getClient();

    /**
     * Check whether the connection is set between HyriAPI and MongoDB or not
     *
     * @return <code>true</code> if the connection is set
     */
    boolean isConnected();

}
