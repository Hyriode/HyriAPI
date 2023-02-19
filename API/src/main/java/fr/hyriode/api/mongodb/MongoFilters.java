package fr.hyriode.api.mongodb;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.regex.Pattern;

/**
 * Created by AstFaster
 * on 15/02/2023 at 16:58
 */
public class MongoFilters {

    /**
     * Creates a filter that matches a value for a field but with case-insensitive
     *
     * @param fieldName The name of the field
     * @param value The value to match
     * @return The created {@link Bson} filter
     */
    public static Bson eqIgn(String fieldName, String value) {
        return Filters.regex(fieldName, Pattern.compile("(?i)^" + value + "$", Pattern.CASE_INSENSITIVE));
    }

    /**
     * Creates a filter that matches all values, without those matching a value for a field with case-insensitive
     *
     * @param fieldName The name of the field
     * @param value The value to match
     * @return The created {@link Bson} filter
     */
    public static Bson notEqIgn(String fieldName, String value) {
        return Filters.not(eqIgn(fieldName, value));
    }

}
