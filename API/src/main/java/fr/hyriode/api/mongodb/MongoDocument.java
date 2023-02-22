package fr.hyriode.api.mongodb;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 13/02/2023 at 00:41
 */
public class MongoDocument extends Document {

    public MongoDocument() {}

    public MongoDocument(String key, Object value) {
        super(key, value);
    }

    public MongoDocument(Map<String, Object> map) {
        super(map);
    }

    /**
     * Append a list of enum
     *
     * @param key The key of the enums
     * @param enums The list of enum
     * @return This {@link MongoDocument} instance
     */
    public MongoDocument appendEnums(String key, Collection<Enum<?>> enums) {
        this.append(key, enums.stream().map(Enum::name).collect(Collectors.toList()));

        return this;
    }

    /**
     * Get a list of enum
     *
     * @param key The key of the enums
     * @param enumClass The class of the enum
     * @return A list of enum
     * @param <T> The type of the enum
     */
    public <T extends Enum<T>> List<Enum<T>> getEnums(String key, Class<T> enumClass) {
        final List<Enum<T>> enums = new ArrayList<>();
        final List<String> enumsStr = this.getList(key, String.class);

        for (String enumStr : enumsStr) {
            enums.add(Enum.valueOf(enumClass, enumStr));
        }
        return enums;
    }

    /**
     * Append a list of {@link MongoSerializable}
     *
     * @param key The key of the data
     * @param serializables The data to serialize
     * @return This {@link MongoDocument} instance
     */
    public MongoDocument appendCollection(String key, Collection<? extends MongoSerializable> serializables) {
        final List<Document> documents = new ArrayList<>();

        for (MongoSerializable serializable : serializables) {
            final MongoDocument document = new MongoDocument();

            serializable.save(document);

            documents.add(document);
        }

        this.append(key, documents);
        return this;
    }

    /**
     * Append a list of {@link MongoSerializable}
     *
     * @param key The key of the data
     * @param serializables The data to serialize
     * @param implClass The implementation class of the serializables
     * @param <T> The type of serializables
     * @param <I> The implementation class of the serializables
     * @return This {@link MongoDocument} instance
     */
    public <T, I extends MongoSerializable> MongoDocument appendCollection(String key, Collection<T> serializables, Class<I> implClass) {
        final List<Document> documents = new ArrayList<>();

        for (T serializable : serializables) {
            final MongoDocument document = new MongoDocument();

            implClass.cast(serializable).save(document);

            documents.add(document);
        }

        this.append(key, documents);
        return this;
    }

    @Override
    public Long getLong(Object key) {
        return ((Number) this.get(key)).longValue();
    }

    /**
     * Create a new {@link MongoDocument} instance from a {@link Document}
     *
     * @param document The document to use
     * @return The created {@link MongoDocument}
     */
    public static MongoDocument of(Document document) {
        return new MongoDocument(document);
    }

}
