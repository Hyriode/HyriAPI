package fr.hyriode.api.impl.common.friend;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.friend.IHyriFriendManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:40
 */
public class HyriFriendManager implements IHyriFriendManager {

    private static final String REDIS_KEY = "friends:";

    @Override
    public List<IHyriFriend> getFriends(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<IHyriFriend> friends = new ArrayList<>();
            final Set<String> jsons = jedis.smembers(REDIS_KEY + playerId.toString());

            for (String json : jsons) {
                friends.add(HyriAPI.GSON.fromJson(json, HyriFriend.class));
            }

            return friends;
        });
    }

    @Override
    public IHyriFriendHandler loadFriends(UUID playerId) {
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

}
