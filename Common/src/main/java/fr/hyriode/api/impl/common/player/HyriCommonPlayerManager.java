package fr.hyriode.api.impl.common.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.model.HyriAccountCreatedEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriPermission;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public abstract class HyriCommonPlayerManager implements IHyriPlayerManager {

    private static final String REDIS_KEY = "players:";
    private static final String IDS_KEY = "uuid:";

    @Override
    public UUID getPlayerId(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String result = jedis.get(this.getIdsKey(name));

            return result != null ? UUID.fromString(result) : null;
        });
    }

    @Override
    public void setPlayerId(String name, UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(this.getIdsKey(name), uuid.toString()));
    }

    @Override
    public void removePlayerId(String name) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(this.getIdsKey(name)));
    }

    @Override
    public IHyriPlayer getPlayer(UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> HyriAPI.GSON.fromJson(jedis.get(this.getPlayersKey(uuid)), HyriPlayer.class));
    }

    @Override
    public IHyriPlayer getPlayer(String name) {
        final UUID uuid = this.getPlayerId(name);

        if (uuid != null) {
            return this.getPlayer(uuid);
        }
        return null;
    }

    @Override
    public IHyriPlayer createPlayer(boolean online, UUID uuid, String name) {
        final IHyriPlayer player = new HyriPlayer(online, name, uuid);

        if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
            player.setRank(EHyriRank.ADMINISTRATOR.get());
        }

        this.setPlayerId(name, uuid);
        this.sendPlayer(player);

        HyriAPI.get().getEventBus().publishAsync(new HyriAccountCreatedEvent(player));

        return player;
    }

    @Override
    public void sendPlayer(IHyriPlayer player) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(this.getPlayersKey(player.getUUID()), HyriAPI.GSON.toJson(player)));
    }

    @Override
    public void removePlayer(UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(this.getPlayersKey(uuid)));
    }

    private String getPlayersKey(UUID uuid) {
        return REDIS_KEY + uuid.toString();
    }

    private String getIdsKey(String name) {
        return IDS_KEY + name.toLowerCase();
    }

    @Override
    public void connectPlayer(UUID uuid, String server) {
        HyriAPI.get().getServerManager().sendPlayerToServer(uuid, server);
    }

    @Override
    public boolean hasPermission(UUID uuid, HyriPermission permission) {
        return this.getPlayer(uuid).getRank().hasPermission(permission);
    }

}
