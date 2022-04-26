package fr.hyriode.api.impl.common.friend;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.HyriFriendRequest;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hydrion.client.module.FriendsModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:40
 */
public class HyriFriendManager implements IHyriFriendManager {

    private static final String REDIS_KEY = "friends:";
    public static final Function<UUID, String> REQUESTS_KEY = playerId -> REDIS_KEY + "requests:" + playerId.toString();
    public static final BiFunction<UUID, UUID, String> REQUESTS_KEY_GETTER = (playerId, sender) -> REQUESTS_KEY.apply(playerId) + ":" + sender.toString();

    private final HydrionManager hydrionManager;
    private FriendsModule friendsModule;

    public HyriFriendManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;

        if (this.hydrionManager.isEnabled()) {
            this.friendsModule = this.hydrionManager.getClient().getFriendsModule();
        }
    }

    private boolean isInCache(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(REDIS_KEY + playerId.toString()));
    }

    @Override
    public List<IHyriFriend> getFriends(UUID playerId) {
        if (this.isInCache(playerId)) {
            return this.getFriendsFromRedis(playerId);
        } else if (this.hydrionManager.isEnabled()) {
            try {
                return this.getFriendsFromHydrion(playerId).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<IHyriFriend> getFriendsFromRedis(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<IHyriFriend> friends = new ArrayList<>();
            final Set<String> jsons = jedis.smembers(REDIS_KEY + playerId.toString());

            for (String json : jsons) {
                friends.add(HyriAPI.GSON.fromJson(json, HyriFriend.class));
            }

            return friends;
        });
    }

    private CompletableFuture<List<IHyriFriend>> getFriendsFromHydrion(UUID playerId) {
        return this.friendsModule.getFriends(playerId).thenApply(response -> {
            final JsonElement content = response.getContent();

            if (!content.isJsonNull()) {
                final HyriFriends friends = HyriAPI.GSON.fromJson(content.getAsJsonObject().get("friends").toString(), HyriFriends.class);

                return friends.getFriends();
            }
            return new ArrayList<>();
        });
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
        final List<IHyriFriend> friends = this.getFriendsFromRedis(playerId);

        if (friends != null) {
            return CompletableFuture.completedFuture(new HyriFriendHandler(playerId, friends));
        }

        if (this.hydrionManager.isEnabled()) {
            return this.getFriendsFromHydrion(playerId).thenApply(list -> new HyriFriendHandler(playerId, list));
        }
        return null;
    }

    @Override
    public void saveFriends(IHyriFriendHandler friendHandler) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final UUID playerId = friendHandler.getOwner();
            final String key = REDIS_KEY + playerId.toString();
            final List<IHyriFriend> oldFriends = this.getFriends(playerId);

            if (oldFriends != null) {
                for (IHyriFriend oldFriend : oldFriends) {
                    if (!friendHandler.areFriends(oldFriend.getUniqueId())) {
                        jedis.srem(key, HyriAPI.GSON.toJson(oldFriend));
                    }
                }
            }

            for (IHyriFriend friend : friendHandler.getFriends()) {
                jedis.sadd(key, HyriAPI.GSON.toJson(friend));
            }
        });
    }

    @Override
    public void updateFriends(IHyriFriendHandler friendHandler) {
        final UUID playerId = friendHandler.getOwner();

        if (this.isInCache(playerId) || HyriAPI.get().getConfiguration().isDevEnvironment()) {
            this.saveFriends(friendHandler);
        } else if (this.hydrionManager.isEnabled()) {
            this.friendsModule.setFriends(playerId, HyriAPI.GSON.toJson(new HyriFriends(friendHandler.getFriends())));
        }
    }

    @Override
    public void removeFriends(UUID playerId) {
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
