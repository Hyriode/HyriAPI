package fr.hyriode.api.mongodb;

/**
 * Created by AstFaster
 * on 12/02/2023 at 21:54.<br>
 *
 * Permits to your object to be serializable as a {@link MongoDocument}.
 */
public interface MongoSerializable {


    /**
     * Write values on a {@link MongoDocument}
     *
     * @param document The {@link MongoDocument} to write
     */
    void save(MongoDocument document);

    /**
     * Read values from a {@link MongoDocument}
     *
     * @param document The {@link MongoDocument} to read
     */
    void load(MongoDocument document);

}
