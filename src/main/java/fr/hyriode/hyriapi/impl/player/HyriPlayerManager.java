package fr.hyriode.hyriapi.impl.player;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import fr.hyriode.hyriapi.impl.rank.EHyriRankImpl;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.rank.HyriPermission;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public record HyriPlayerManager(HyriAPIPlugin plugin) implements IHyriPlayerManager {

    private static final String REDIS_KEY = "players:";
    private static final String IDS_KEY = "uuid:";

    @Override
    public UUID getPlayerId(String name) {
        final Jedis jedis = HyriAPI.get().getRedisResource();

        UUID uuid = null;
        try {
            final String result = jedis.get(this.getIdsKey(name));

            if (result != null) {
                uuid = UUID.fromString(result);
            }
        } finally {
            jedis.close();
        }

        return uuid;
    }

    @Override
    public void setPlayerId(String name, UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(this.getIdsKey(name), uuid.toString()));
    }

    @Override
    public IHyriPlayer getPlayer(UUID uuid) {
        final Jedis jedis = HyriAPI.get().getRedisResource();

        IHyriPlayer player;
        try {
            player = HyriAPIPlugin.GSON.fromJson(jedis.get(this.getPlayersKey(uuid)), HyriPlayer.class);
        } finally {
            jedis.close();
        }

        return player;
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
    public IHyriPlayer createPlayer(UUID uuid, String name) {
        final IHyriPlayer hyriPlayer = new HyriPlayer(name, uuid);

        if (this.plugin.getConfiguration().isDevEnvironment()) {
            hyriPlayer.setRank(EHyriRankImpl.ADMINISTRATOR.get());
        }

        this.setPlayerId(name, uuid);
        this.sendPlayer(hyriPlayer);

        return hyriPlayer;
    }

    @Override
    public void sendPlayer(IHyriPlayer player) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(this.getPlayersKey(player.getUUID()), HyriAPIPlugin.GSON.toJson(player)));
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
    public void kickPlayer(UUID uuid, String reason) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.kickPlayer(reason);
        }
    }

    @Override
    public void connectPlayer(UUID uuid, String server) {
        HyriAPI.get().getServerManager().sendPlayerToServer(uuid, server);
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public int getPing(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            return ((CraftPlayer) player).getHandle().ping;
        }
        return -1;
    }

    @Override
    public boolean hasPermission(UUID uuid, HyriPermission permission) {
        return this.getPlayer(uuid).getRank().hasPermission(permission);
    }

}
