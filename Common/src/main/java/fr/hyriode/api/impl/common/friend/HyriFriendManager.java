package fr.hyriode.api.impl.common.friend;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.HyriFriendRequest;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.mongodb.subscriber.OperationSubscriber;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:40
 */
public class HyriFriendManager implements IHyriFriendManager {

    private static final String REDIS_KEY = "friends:";
    public static final Function<UUID, String> REQUESTS_KEY = playerId -> REDIS_KEY + "requests:" + playerId.toString();
    public static final BiFunction<UUID, UUID, String> REQUESTS_KEY_GETTER = (playerId, sender) -> REQUESTS_KEY.apply(playerId) + ":" + sender.toString();

    private static final Function<UUID, Bson> FRIENDS_FILTER = uuid -> Filters.eq("uuid", uuid.toString());

    private final Supplier<MongoCollection<BasicDBObject>> friendsCollection;

    public HyriFriendManager(HyriCommonImplementation api) {
        this.friendsCollection = () -> api.getPlayerManager().getPlayersDatabase().getCollection("friends", BasicDBObject.class);
    }

    private boolean isInCache(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(REDIS_KEY + playerId.toString()));
    }

    @Override
    public List<IHyriFriend> getFriends(UUID playerId) {
        return this.isInCache(playerId) ? this.getFriendsFromCache(playerId) : this.getFriendsFromMongo(playerId);
    }

    private List<IHyriFriend> getFriendsFromCache(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(REDIS_KEY + playerId.toString());

            if (json != null) {
                return HyriAPI.GSON.fromJson(json, HyriFriends.class).getFriends();
            }
            return null;
        });
    }

    private List<IHyriFriend> getFriendsFromMongo(UUID playerId) {
        final OperationSubscriber<BasicDBObject> subscriber = new OperationSubscriber<>();

        this.friendsCollection.get().find(FRIENDS_FILTER.apply(playerId)).subscribe(subscriber);

        final BasicDBObject dbObject = subscriber.get();

        if (dbObject == null) {
            return null;
        }

        final HyriFriends friends = HyriAPI.GSON.fromJson(dbObject.toJson(), HyriFriends.class);

        if (friends == null) {
            return null;
        }
        return friends.getFriends();
    }

    @Override
    public IHyriFriendHandler createHandler(UUID playerId) {
        List<IHyriFriend> friends = this.getFriends(playerId);

        if (friends == null) {
            friends = new ArrayList<>();
        }
        return new HyriFriendHandler(playerId, friends);
    }

    @Override
    public CompletableFuture<IHyriFriendHandler> createHandlerAsync(UUID playerId) {
        return CompletableFuture.completedFuture(new HyriFriendHandler(playerId, this.getFriends(playerId)));
    }

    @Override
    public void saveFriendsInCache(IHyriFriendHandler friendHandler) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(REDIS_KEY + friendHandler.getOwner().toString(), HyriAPI.GSON.toJson(new HyriFriends(friendHandler))));
    }

    @Override
    public void updateFriends(IHyriFriendHandler friendHandler) {
        final UUID playerId = friendHandler.getOwner();

        if (HyriAPI.get().getPlayerManager().getPlayer(playerId).isOnline()) {
            this.saveFriendsInCache(friendHandler);
        } else if (this.getFriendsFromMongo(playerId) == null) {
            this.friendsCollection.get().insertOne(BasicDBObject.parse(HyriAPI.GSON.toJson(new HyriFriends(friendHandler)))).subscribe(new OperationSubscriber<>());
        } else {
            this.friendsCollection.get().replaceOne(FRIENDS_FILTER.apply(playerId), BasicDBObject.parse(HyriAPI.GSON.toJson(new HyriFriends(friendHandler)))).subscribe(new OperationSubscriber<>());
        }
    }

    @Override
    public void removeCachedFriends(UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(REDIS_KEY + playerId.toString()));
    }

    @Override
    public void sendRequest(UUID sender, UUID target) {
        final HyriFriendRequest invitation = new HyriFriendRequest(sender, target);

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final String key = REQUESTS_KEY_GETTER.apply(target, sender);

            jedis.set(key, HyriAPI.GSON.toJson(invitation));
            jedis.expire(key, 60);
        });

        HyriAPI.get().getPubSub().send(REDIS_CHANNEL, new HyriFriendRequest.Packet(invitation));
    }

    @Override
    public void removeRequest(UUID player, UUID sender) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.del(REQUESTS_KEY_GETTER.apply(player, sender)));
    }

    @Override
    public boolean hasRequest(UUID player, UUID sender) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(REQUESTS_KEY_GETTER.apply(player, sender)) != null);
    }

    @Override
    public List<HyriFriendRequest> getRequests(UUID player) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<HyriFriendRequest> requests = new ArrayList<>();
            final Set<String> keys = jedis.keys(REQUESTS_KEY.apply(player) + ":*");

            for (String key : keys) {
                requests.add(HyriAPI.GSON.fromJson(jedis.get(key), HyriFriendRequest.class));
            }
            return requests;
        });
    }

}
