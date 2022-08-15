package fr.hyriode.api.impl.common.host;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import fr.hyriode.api.host.IHostConfig;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 07/08/2022 at 12:52
 */
public class HostConfig implements IHostConfig {

    private final String id;
    private final long creationDate;
    private final UUID owner;
    private final String game;
    private final String gameType;
    private String name;
    private String icon;
    private boolean private_;

    private final Map<String, Object> values;

    public HostConfig(String id, UUID owner, String game, String gameType, String name, String icon) {
        this.id = id;
        this.creationDate = System.currentTimeMillis();
        this.owner = owner;
        this.game = game;
        this.gameType = gameType;
        this.name = name;
        this.icon = icon;
        this.values = new HashMap<>();
    }

    private HostConfig(String id, long creationDate, UUID owner, String game, String gameType, String name, String icon, boolean private_, Map<String, Object> values) {
        this.id = id;
        this.creationDate = creationDate;
        this.owner = owner;
        this.game = game;
        this.gameType = gameType;
        this.name = name;
        this.icon = icon;
        this.private_ = private_;
        this.values = values;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public long getCreationDate() {
        return this.creationDate;
    }

    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public String getGame() {
        return this.game;
    }

    @Override
    public String getGameType() {
        return this.gameType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean isPrivate() {
        return this.private_;
    }

    @Override
    public void setPrivate(boolean value) {
        this.private_ = value;
    }

    @Override
    public void addValue(String key, Object object) {
        this.values.put(key, object);
    }

    @Override
    public void removeValue(String key) {
        this.values.remove(key);
    }

    @Override
    public Object getValue(String key) {
        return this.values.get(key);
    }

    @Override
    public Map<String, Object> getValues() {
        return this.values;
    }

    public static class Serializer implements JsonSerializer<HostConfig>, JsonDeserializer<HostConfig> {

        @Override
        public HostConfig deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            final JsonObject object = jsonElement.getAsJsonObject();
            final String id = object.get("id").getAsString();
            final long creationDate = object.get("creation-date").getAsLong();
            final UUID owner = UUID.fromString(object.get("owner").getAsString());
            final String game = object.get("game").getAsString();
            final String gameType = object.get("game-type").getAsString();
            final String name = object.get("name").getAsString();
            final String icon = object.get("icon").getAsString();
            final JsonElement privateElement = object.get("private");
            final boolean private_ = privateElement != null && privateElement.getAsBoolean();
            final Map<String, Value> serializedValues = ctx.deserialize(object.get("values"), new TypeToken<Map<String, Value>>() {}.getType());
            final Map<String, Object> values = new HashMap<>();

            for (Map.Entry<String, Value> entry : serializedValues.entrySet()) {
                final Value value = entry.getValue();
                try {
                    final Class<?> clazz = Class.forName(value.getClassName());

                    values.put(entry.getKey(), ctx.deserialize(value.getObject(), clazz));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return new HostConfig(id, creationDate, owner, game, gameType, name, icon, private_, values);
        }

        @Override
        public JsonElement serialize(HostConfig hostConfig, Type type, JsonSerializationContext ctx) {
            final JsonObject object = new JsonObject();
            final Map<String, Value> values = new HashMap<>();

            for (Map.Entry<String, Object> entry : hostConfig.getValues().entrySet()) {
                final Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                values.put(entry.getKey(), new Value(value.getClass().getName(), ctx.serialize(value)));
            }

            object.addProperty("id", hostConfig.getId());
            object.addProperty("creation-date", hostConfig.getCreationDate());
            object.addProperty("owner", hostConfig.getOwner().toString());
            object.addProperty("game", hostConfig.getGame());
            object.addProperty("game-type", hostConfig.getGameType());
            object.addProperty("name", hostConfig.getName());
            object.addProperty("icon", hostConfig.getIcon());
            object.addProperty("private", hostConfig.isPrivate());
            object.add("values", ctx.serialize(values));

            return object;
        }

    }

    private static class Value {

        private final String className;
        private final JsonElement object;

        public Value(String className, JsonElement object) {
            this.className = className;
            this.object = object;
        }

        public String getClassName() {
            return this.className;
        }

        public JsonElement getObject() {
            return this.object;
        }

    }

}
