package fr.hyriode.api.impl.common.friend;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.HyriFriendRequest;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.friend.IHyriFriendManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

    @Override
    public List<IHyriFriend> getFriends(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(REDIS_KEY + playerId.toString());

            if (json != null) {
                return HyriAPI.GSON.fromJson(json, HyriFriends.class).getFriends();
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
    public void saveFriends(IHyriFriendHandler friendHandler) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(REDIS_KEY + friendHandler.getOwner().toString(), HyriAPI.GSON.toJson(new HyriFriends(friendHandler))));
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
