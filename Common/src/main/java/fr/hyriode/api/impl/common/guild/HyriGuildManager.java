package fr.hyriode.api.impl.common.guild;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.guild.IHyriGuild;
import fr.hyriode.api.guild.IHyriGuildManager;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoFilters;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.player.IHyriPlayer;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Pipeline;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 13/02/2023 at 00:18
 */
public class HyriGuildManager implements IHyriGuildManager {

    private static final long TTL = TimeUnit.HOURS.toSeconds(48);

    // Main key of guilds
    private static final String KEY = "guilds:";
    // Secondary index for guilds: name -> id
    private static final String NAMES_KEY = "guilds-names:";
    // Secondary index for guilds: tag -> id
    private static final String TAGS_KEY = "guilds-tags:";

    private final MongoCollection<Document> guildsCollection;

    public HyriGuildManager() {
        this.guildsCollection = HyriAPI.get().getMongoDB().getDatabase("guilds").getCollection("guilds");
    }

    private void cacheGuild(HyriGuild guild) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final byte[] key = (KEY + guild.getId().toHexString()).getBytes(StandardCharsets.UTF_8);

            pipeline.set(key, HyriAPI.get().getDataSerializer().serialize(guild));
            pipeline.expire(key, TTL);
            pipeline.sync();
        });
    }

    @Override
    public @NotNull IHyriGuild createGuild(@NotNull UUID playerId, @NotNull String name) {
        final HyriGuild guild = new HyriGuild(name, playerId);
        final IHyriPlayer account = IHyriPlayer.get(playerId);

        account.setGuild(guild.getId());
        account.update();

        this.guildsCollection.insertOne(MongoSerializer.serialize(guild));
        this.cacheGuild(guild);

        return guild;
    }

    @Override
    public @Nullable IHyriGuild getGuild(@NotNull ObjectId guildId) {
        // Try first to find the guild in cache
        final HyriGuild cachedGuild = HyriAPI.get().getRedisProcessor().get(jedis -> {
            final byte[] key = (KEY + guildId.toHexString()).getBytes(StandardCharsets.UTF_8);
            final byte[] serialized = jedis.get(key);

            return serialized == null ? null : HyriAPI.get().getDataSerializer().deserialize(new HyriGuild(), serialized);
        });

        if (cachedGuild != null) {
            return cachedGuild;
        }

        // Guild wasn't found in cache, let's query MongoDB
        final Document document = this.guildsCollection.find(Filters.eq("_id", guildId)).first();

        if (document == null) {
            return null;
        }

        final HyriGuild guild = new HyriGuild();

        guild.load(new MongoDocument(document));

        // Add it in cache for next times
        this.cacheGuild(guild);

        return guild;
    }

    @Override
    public @Nullable IHyriGuild getGuild(@NotNull String name) {
        // Try first to find the guild in cache
        final HyriGuild cachedGuild = (HyriGuild) HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String idHex = jedis.get(NAMES_KEY + name.toLowerCase());

            return idHex == null ? null : this.getGuild(new ObjectId(idHex));
        });

        if (cachedGuild != null) {
            return cachedGuild;
        }

        // Identifier wasn't found in cache, let's query MongoDB
        final Document document = this.guildsCollection.find(MongoFilters.eqIgn("name", name)).first();

        if (document == null) {
            return null;
        }

        final HyriGuild guild = new HyriGuild();

        guild.load(new MongoDocument(document));

        // Add it in cache for next times
        this.cacheGuild(guild);

        // Add secondary key in cache
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final String key = NAMES_KEY + name.toLowerCase();

            pipeline.set(key, guild.getId().toHexString());
            pipeline.expire(key, TTL);
            pipeline.sync();
        });
        return guild;
    }

    @Override
    public void saveGuild(@NotNull IHyriGuild in) {
        final HyriGuild guild = (HyriGuild) in;

        // Save it in cache
        this.cacheGuild(guild);
        // Replace the document in MongoDB
        this.guildsCollection.replaceOne(Filters.eq("_id", guild.getId()), MongoSerializer.serialize(guild));
    }

    @Override
    public void removeGuild(@NotNull ObjectId guildId) {
        // Delete it from cache
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY + guildId.toHexString()));

        // Delete if from MongoDB
        this.guildsCollection.deleteOne(Filters.eq("_id", guildId));
    }

    @Override
    public @Nullable ObjectId getGuildIdFromName(@NotNull String name) {
        // Try to find it from cache
        final ObjectId cachedId = HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String idHex = jedis.get(NAMES_KEY + name.toLowerCase());

            return idHex == null ? null : new ObjectId(idHex);
        });

        if (cachedId != null) {
            return cachedId;
        }

        // Nothing was found in the cache, let's query MongoDB
        final Document document = this.guildsCollection.find(MongoFilters.eqIgn("name", name))  // Find it with case-insensitive
                .projection(Projections.fields(Projections.include("_id")))
                .first();

        if (document == null) {
            return null;
        }

        final ObjectId id = document.getObjectId("_id");

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final String key = NAMES_KEY + name.toLowerCase();

            pipeline.set(key, id.toHexString());
            pipeline.expire(key, TTL);
            pipeline.sync();
        });
        return id;
    }

    @Override
    public @Nullable ObjectId getGuildIdFromTag(@NotNull String tag) {
        // Try to find it from cache
        final ObjectId cachedId = HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String idHex = jedis.get(TAGS_KEY + tag);

            return idHex == null ? null : new ObjectId(idHex);
        });

        if (cachedId != null) {
            return cachedId;
        }

        // Nothing was found in the cache, lets query MongoDB
        final Document document = this.guildsCollection.find(Filters.eq("tag", tag))
                .projection(Projections.fields(Projections.include("_id")))
                .first();

        if (document == null) {
            return null;
        }

        final ObjectId id = document.getObjectId("_id");

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final String key = TAGS_KEY + tag;

            pipeline.set(key, id.toHexString());
            pipeline.expire(key, TTL);
            pipeline.sync();
        });
        return id;
    }

    @Override
    public boolean isNameExisting(@NotNull String name) {
        return this.getGuildIdFromName(name) != null;
    }

    @Override
    public boolean isTagExisting(@NotNull String tag) {
        return this.getGuildIdFromTag(tag) != null;
    }

}
