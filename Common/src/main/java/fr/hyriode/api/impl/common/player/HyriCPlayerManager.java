package fr.hyriode.api.impl.common.player;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.packet.PlayerComponentPacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.event.model.HyriAccountCreatedEvent;
import fr.hyriode.api.impl.common.player.nickname.HyriNicknameManager;
import fr.hyriode.api.impl.common.player.packet.HyriPlayerKickPacket;
import fr.hyriode.api.impl.common.player.title.PlayerTitlePacket;
import fr.hyriode.api.impl.common.player.title.TitlePacket;
import fr.hyriode.api.impl.common.whitelist.HyriWhitelistManager;
import fr.hyriode.api.mongodb.subscriber.CallbackSubscriber;
import fr.hyriode.api.mongodb.subscriber.OperationSubscriber;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.model.HyriSendPlayerPacket;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.nickname.IHyriNicknameManager;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.whitelist.IHyriWhitelistManager;
import org.bson.conversions.Bson;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public abstract class HyriCPlayerManager implements IHyriPlayerManager {

    private static final Function<UUID, String> PLAYERS_KEY = uuid -> "players:" + uuid.toString();
    private static final Function<String, String> IDS_KEY = name -> "uuid:" + name.toLowerCase();
    private static final Function<UUID, String> PREFIX_KEY = uuid -> "players-prefix:" + uuid.toString();
    private static final Function<UUID, Bson> ACCOUNTS_FILTER = uuid -> Filters.eq("uuid", uuid.toString());

    private final MongoDatabase playersDatabase;
    private final MongoCollection<BasicDBObject> accountsCollection;

    private final IHyriNicknameManager nicknameManager;
    private final IHyriWhitelistManager whitelistManager;

    public HyriCPlayerManager() {
        this.nicknameManager = new HyriNicknameManager();
        this.whitelistManager = new HyriWhitelistManager();
        this.playersDatabase = HyriAPI.get().getMongoDB().getDatabase("players");
        this.accountsCollection = this.playersDatabase.getCollection("accounts", BasicDBObject.class);
    }

    @Override
    public UUID getPlayerId(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String result = jedis.get(IDS_KEY.apply(name));

            return result != null ? UUID.fromString(result) : null;
        });
    }

    @Override
    public void setPlayerId(String name, UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(IDS_KEY.apply(name), uuid.toString()));
    }

    @Override
    public void removePlayerId(String name) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(IDS_KEY.apply(name)));
    }

    @Override
    public IHyriPlayer createPlayer(boolean online, UUID uuid, String name) {
        final HyriPlayer player = new HyriPlayer(online, name, uuid);

        if (HyriAPI.get().getConfig().isDevEnvironment()) {
            final HyriRank rank = new HyriRank(HyriPlayerRankType.PLAYER);

            rank.setStaffType(HyriStaffRankType.ADMINISTRATOR);

            player.setRank(rank);
        }

        this.accountsCollection.insertOne(BasicDBObject.parse(HyriAPI.GSON.toJson(player))).subscribe(new OperationSubscriber<>());

        this.updateCachedPlayer(player);
        this.setPlayerId(name, uuid);

        HyriAPI.get().getEventBus().publishAsync(new HyriAccountCreatedEvent(player));

        return player;
    }

    @Override
    public IHyriPlayer getPlayer(UUID uuid) {
        final IHyriPlayer player = this.getCachedPlayer(uuid);

        if (player == null) {
            final OperationSubscriber<BasicDBObject> subscriber = new OperationSubscriber<>();

            this.accountsCollection.find(ACCOUNTS_FILTER.apply(uuid))
                    .first()
                    .subscribe(subscriber);

            final BasicDBObject dbObject = subscriber.get();

            return dbObject == null ? null : HyriAPI.GSON.fromJson(dbObject.toJson(), HyriPlayer.class);
        }
        return player;
    }

    @Override
    public void updatePlayer(IHyriPlayer player) {
        this.accountsCollection.replaceOne(ACCOUNTS_FILTER.apply(player.getUniqueId()), BasicDBObject.parse(HyriAPI.GSON.toJson(player))).subscribe(new OperationSubscriber<>());
    }

    @Override
    public void removePlayer(UUID uuid) {
        this.accountsCollection.deleteOne(ACCOUNTS_FILTER.apply(uuid)).subscribe(new OperationSubscriber<>());
    }

    @Override
    public IHyriPlayer getCachedPlayer(UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> HyriAPI.GSON.fromJson(jedis.get(PLAYERS_KEY.apply(uuid)), HyriPlayer.class));
    }

    @Override
    public void updateCachedPlayer(IHyriPlayer player) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PLAYERS_KEY.apply(player.getUniqueId()), HyriAPI.GSON.toJson(player)));
    }

    @Override
    public void removeCachedPlayer(UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PLAYERS_KEY.apply(uuid)));
    }

    @Override
    public void kickPlayer(UUID uuid, String component) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new HyriPlayerKickPacket(uuid, component));
    }

    @Override
    public void connectPlayer(UUID uuid, String server) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new HyriSendPlayerPacket(uuid, server));
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new PlayerMessagePacket(uuid, message));
    }

    @Override
    public void sendComponent(UUID uuid, String component) {
        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new PlayerComponentPacket(uuid, component));
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
    public String getCachedPrefix(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(PREFIX_KEY.apply(playerId)));
    }

    @Override
    public String getPrefix(UUID playerId) {
        String prefix = this.getCachedPrefix(playerId);

        if (prefix != null) {
            return prefix;
        }

        prefix = this.getPlayer(playerId).getNameWithRank();

        this.savePrefix(playerId, prefix);

        return prefix;
    }

    @Override
    public void savePrefix(UUID playerId, String prefix) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PREFIX_KEY.apply(playerId), prefix));
    }

    @Override
    public IHyriNicknameManager getNicknameManager() {
        return this.nicknameManager;
    }

    @Override
    public IHyriWhitelistManager getWhitelistManager() {
        return this.whitelistManager;
    }

    public MongoDatabase getPlayersDatabase() {
        return this.playersDatabase;
    }

}
