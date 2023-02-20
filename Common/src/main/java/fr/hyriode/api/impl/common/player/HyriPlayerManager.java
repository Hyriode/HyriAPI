package fr.hyriode.api.impl.common.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.packet.BroadcastMessagePacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.impl.common.player.packet.PlayerKickPacket;
import fr.hyriode.api.impl.common.player.packet.PlayerTitlePacket;
import fr.hyriode.api.impl.common.player.packet.TitlePacket;
import fr.hyriode.api.impl.common.whitelist.HyriWhitelistManager;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.model.PlayerServerSendPacket;
import fr.hyriode.api.player.IHyriNicknameManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.rank.StaffRank;
import fr.hyriode.api.whitelist.IHyriWhitelistManager;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Pipeline;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayerManager implements IHyriPlayerManager {

    private static final long TTL = TimeUnit.HOURS.toSeconds(48);

    // The key for primary indexing: uuid -> accounts
    private static final String PLAYERS_KEY = "players:";
    // The key for secondary indexing: name -> uuid
    private static final String IDS_KEY = "uuid:";
    // The key for players sessions
    private static final String SESSIONS_KEY = "players-sessions:";

    private final IHyriNicknameManager nicknameManager;
    private final IHyriWhitelistManager whitelistManager;

    private final MongoCollection<Document> accountsCollection;

    public HyriPlayerManager() {
        this.nicknameManager = new HyriNicknameManager();
        this.whitelistManager = new HyriWhitelistManager();
        this.accountsCollection = HyriAPI.get().getMongoDB().getDatabase("players").getCollection("accounts");
    }

    private void cachePlayer(HyriPlayer player) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final byte[] key = (PLAYERS_KEY + player.getUniqueId().toString()).getBytes(StandardCharsets.UTF_8);

            pipeline.set(key, HyriAPI.get().getDataSerializer().serialize(player));
            pipeline.expire(key, TTL);
            pipeline.sync();
        });
    }

    @Override
    public UUID getPlayerId(String name) {
        final UUID cachedId = HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String result = jedis.get(IDS_KEY + name.toLowerCase());

            return result != null ? UUID.fromString(result) : null;
        });

        if (cachedId != null) {
            return cachedId;
        }

        // Player id wasn't found in cache, let's query MongoDB
        final Document document = this.accountsCollection.find(Filters.eq("name", name))
                .projection(Projections.fields(Projections.include("_id")))
                .first();

        if (document == null) {
            return null;
        }

        final UUID uuid = UUID.fromString(document.getString("_id"));

        this.savePlayerId(name, uuid);

        return uuid;
    }

    @Override
    public void savePlayerId(String name, UUID uuid) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final String key = IDS_KEY + name.toLowerCase();

            pipeline.set(key, uuid.toString());
            pipeline.expire(key, TTL);
            pipeline.sync();
        });
    }

    @Override
    public void deletePlayerId(String name) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(IDS_KEY + name.toLowerCase()));
    }

    @Override
    public IHyriPlayer createPlayer(boolean premium, UUID uuid, String name) {
        final HyriPlayer player = new HyriPlayer(premium,  name, uuid);

        if (HyriAPI.get().getConfig().isDevEnvironment()) {
            player.getRank().setStaffType(StaffRank.ADMINISTRATOR);
        }

        this.accountsCollection.insertOne(MongoSerializer.serialize(player));
        this.cachePlayer(player);

        return player;
    }

    @Override
    public IHyriPlayer getPlayer(UUID uuid) {
        final IHyriPlayer cachedPlayer = HyriAPI.get().getRedisProcessor().get(jedis -> {
            final byte[] key = (PLAYERS_KEY + uuid.toString()).getBytes(StandardCharsets.UTF_8);
            final byte[] bytes = jedis.get(key);

            return bytes == null ? null : HyriAPI.get().getDataSerializer().deserialize(new HyriPlayer(), bytes);
        });

        if (cachedPlayer != null) {
            return cachedPlayer;
        }

        // Player wasn't found in cache, let's query MongoDB
        final Document document = this.accountsCollection.find(Filters.eq("_id", uuid.toString())).first();

        if (document == null) {
            return null;
        }

        final HyriPlayer player = new HyriPlayer();

        player.load(MongoDocument.of(document));

        // Add it in cache for next times
        this.cachePlayer(player);

        return player;
    }

    @Override
    public void savePlayer(IHyriPlayer in) {
        final HyriPlayer player = (HyriPlayer) in;

        // Save it in cache
        this.cachePlayer(player);
        // Replace the document in MongoDB
        this.accountsCollection.replaceOne(Filters.eq("_id", player.getUniqueId().toString()), MongoSerializer.serialize(player));
    }

    @Override
    public void removePlayer(UUID uuid) {
        // Delete it from cache
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PLAYERS_KEY + uuid.toString()));

        // Delete if from MongoDB
        this.accountsCollection.deleteOne(Filters.eq("_id", uuid.toString()));
    }

    @Override
    public List<IHyriPlayer> getPlayers() {
        final List<IHyriPlayer> players = new ArrayList<>();

        try (final MongoCursor<Document> iterator =  this.accountsCollection.find().iterator()) {
            while (iterator.hasNext()) {
                final HyriPlayer player = new HyriPlayer();

                player.load(MongoDocument.of(iterator.next()));

                players.add(player);
            }
        }
        return players;
    }

    @Override
    public @Nullable IHyriPlayerSession getSession(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final byte[] key = (SESSIONS_KEY + playerId.toString()).getBytes(StandardCharsets.UTF_8);
            final byte[] data = jedis.get(key);

            return data == null ? null : HyriAPI.get().getDataSerializer().deserialize(new HyriPlayerSession(), data);
        });
    }

    @Override
    public void updateSession(@NotNull IHyriPlayerSession session) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final byte[] key = (SESSIONS_KEY + session.getPlayerId().toString()).getBytes(StandardCharsets.UTF_8);

            jedis.set(key, HyriAPI.get().getDataSerializer().serialize((HyriPlayerSession) session));
        });
    }

    @Override
    public void deleteSession(@NotNull UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del((SESSIONS_KEY + playerId).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void kickPlayer(UUID uuid, String component) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new PlayerKickPacket(uuid, component));
    }

    @Override
    public void connectPlayer(UUID uuid, String server) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new PlayerServerSendPacket(uuid, server));
    }

    @Override
    public void broadcastMessage(String message, boolean component) {
        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new BroadcastMessagePacket(message, component));
    }

    @Override
    public void sendMessage(UUID uuid, String message, boolean component) {
        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new PlayerMessagePacket(uuid, message, component));
    }

    @Override
    public void sendTitle(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new PlayerTitlePacket(uuid, title, subtitle, fadeIn, stay, fadeOut));
    }

    @Override
    public void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new TitlePacket(title, subtitle, fadeIn, stay, fadeOut));
    }

    @Override
    public int getPing(UUID uuid) {
        return -1;
    }

    @Override
    public IHyriNicknameManager getNicknameManager() {
        return this.nicknameManager;
    }

    @Override
    public IHyriWhitelistManager getWhitelistManager() {
        return this.whitelistManager;
    }

}
