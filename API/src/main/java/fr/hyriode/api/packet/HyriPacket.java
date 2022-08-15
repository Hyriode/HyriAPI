package fr.hyriode.api.packet;

import com.google.gson.*;
import fr.hyriode.api.HyriAPI;

import java.lang.reflect.Type;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/11/2021 at 11:37
 */
public abstract class HyriPacket {

    public static class Serializer implements JsonSerializer<HyriPacket>, JsonDeserializer<HyriPacket> {

        private static final String CLASS = "class";
        private static final String CONTENT = "content";

        @Override
        public JsonElement serialize(HyriPacket src, Type typeOfSrc, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();

            object.addProperty(CLASS, src.getClass().getName());
            object.addProperty(CONTENT, HyriAPI.NORMAL_GSON.toJson(src));

            return object;
        }

        @Override
        public HyriPacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject object = json.getAsJsonObject();
            final String className = object.get(CLASS).getAsString();

            try {
                final Class<?> clazz = Class.forName(className);
                final String content = object.get(CONTENT).getAsString();

                return (HyriPacket) HyriAPI.NORMAL_GSON.fromJson(content, clazz);
            } catch (ClassNotFoundException ignored) {}
            return null;
        }

    }

}
