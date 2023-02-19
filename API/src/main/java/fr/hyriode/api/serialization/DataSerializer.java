package fr.hyriode.api.serialization;

/**
 * Created by AstFaster
 * on 15/02/2023 at 15:49.<br>
 *
 * The serializer of {@link DataSerializable} objects.
 */
public interface DataSerializer {

    /**
     * Serialize an object
     *
     * @param object The object to serialize
     * @param <T> The type of the object
     * @return The serialized object
     */
    <T extends DataSerializable> byte[] serialize(T object);

    /**
     * Deserialize an object
     *
     * @param object The object to deserialize
     * @param bytes The bytes to read
     * @param <T> The type of the object
     * @return The deserialized object
     */
    <T extends DataSerializable> T deserialize(T object, byte[] bytes);

}
