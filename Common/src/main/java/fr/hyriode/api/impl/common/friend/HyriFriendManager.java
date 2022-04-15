package fr.hyriode.api.impl.common.friend;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.hydrion.client.module.FriendsModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:40
 */
public class HyriFriendManager implements IHyriFriendManager {

    private static final String REDIS_KEY = "friends:";

    private final HydrionManager hydrionManager;
    private final FriendsModule friendsModule;

    public HyriFriendManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;
        this.friendsModule = this.hydrionManager.getClient().getFriendsModule();
    }

    @Override
    public List<IHyriFriend> getFriends(UUID playerId) {
        final String key = REDIS_KEY + playerId.toString();

        if (HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(key))) {
            return HyriAPI.get().getRedisProcessor().get(jedis -> {
                final List<IHyriFriend> friends = new ArrayList<>();
                final Set<String> jsons = jedis.smembers(key);

                for (String json : jsons) {
                    friends.add(HyriAPI.GSON.fromJson(json, HyriFriend.class));
                }

                return friends;
            });
        } else if (this.hydrionManager.isEnabled()) {
            try {
                return this.friendsModule.getFriends(playerId).thenApply(response -> {
                    final JsonElement content = response.getContent();

                    if (!content.isJsonNull()) {
                        return HyriAPI.GSON.fromJson(content.getAsJsonArray(), new TypeToken<List<HyriFriend>>(){}.getType());
                    }
                    return new ArrayList<IHyriFriend>();
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public IHyriFriendHandler createHandler(UUID playerId) {
        return new HyriFriendHandler(playerId, this.getFriends(playerId));
    }

    @Override
    public void updateFriends(IHyriFriendHandler friendHandler) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final UUID playerId = friendHandler.getOwner();
            final String key = REDIS_KEY + playerId.toString();
            final List<IHyriFriend> oldFriends = this.getFriends(playerId);

            for (IHyriFriend oldFriend : oldFriends) {
                if (!friendHandler.areFriends(oldFriend.getUniqueId())) {
                    jedis.srem(key, HyriAPI.GSON.toJson(oldFriend));
                }
            }

            for (IHyriFriend friend : friendHandler.getFriends()) {
                jedis.sadd(key, HyriAPI.GSON.toJson(friend));
            }
        });
    }

    @Override
    public void removeFriends(UUID playerId) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.del(REDIS_KEY + playerId.toString()));
    }

}
