package fr.hyriode.hyriapi.tools.npc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class NPCProfileLoader {

    private final String skinOwner;
    private final String name;

    public NPCProfileLoader(String name, String skinOwner) {
        this.name = name;
        this.skinOwner = this.getUUID(skinOwner);
    }

    public GameProfile loadProfile(Jedis jedis) {
        final UUID uuid = UUID.fromString(this.skinOwner);
        final GameProfile skinProfile = new GameProfile(UUID.randomUUID(), this.name);
        final String key = "cacheSkin:";
        final String json = jedis.get(key + uuid);

        GameProfile profile;
        if (json == null) {
            profile = MinecraftServer.getServer().aD().fillProfileProperties(new GameProfile(uuid, null), true);

            if (profile.getName() != null) {
                final JsonArray jsonArray = new JsonArray();

                for (Property property : profile.getProperties().values()) {
                    jsonArray.add(new Gson().toJsonTree(property));
                }

                jedis.set(key + uuid, jsonArray.toString());
                jedis.expire(key + uuid, 172800L);
            }

            skinProfile.getProperties().putAll(profile.getProperties());
        } else {
            final JsonArray parse = new JsonParser().parse(json).getAsJsonArray();

            for (JsonElement element : parse) {
                final Property property = new Gson().fromJson(element.toString(), Property.class);

                skinProfile.getProperties().put(property.getName(), property);
            }
        }

        return skinProfile;
    }

    @SuppressWarnings("deprecation")
    private String getUUID(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId().toString();
    }

}
