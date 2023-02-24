package fr.hyriode.api.serialization;

import com.google.gson.*;
import fr.hyriode.hyggdrasil.api.HyggdrasilAPI;

import java.lang.reflect.Type;

/**
 * Created by AstFaster
 * on 23/10/2022 at 15:41
 */
public class ClassSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String CLASS = "class";
    private static final String CONTENT = "content";

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject object = new JsonObject();

        object.addProperty(CLASS, src.getClass().getName());
        object.addProperty(CONTENT, HyggdrasilAPI.NORMAL_GSON.toJson(src));

        return object;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            final JsonObject object = json.getAsJsonObject();
            final Class<?> clazz = Class.forName(object.get(CLASS).getAsString());
            final String content = object.get(CONTENT).getAsString();

            return HyggdrasilAPI.NORMAL_GSON.fromJson(content, (Type) clazz);
        } catch (ClassNotFoundException ignored) {}
        return null;
    }

}
