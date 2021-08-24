package fr.hyriode.hyriapi.impl.api.player;

import com.google.gson.Gson;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriPlugin;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 22:22
 */
public class HyriPlayerManager implements IHyriPlayerManager {

    private static final String REDIS_KEY = "players:";

    private final HyriAPI api;

    private final HyriPlugin plugin;

    public HyriPlayerManager(HyriPlugin plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getAPI();
    }

    /**
     * Redis player management
     */

    @Override
    public IHyriPlayer getPlayer(UUID uuid) {
        return new Gson().fromJson(this.getJedis().get(this.getJedisKey(uuid)), HyriPlayer.class);
    }

    @Override
    public IHyriPlayer createPlayer(UUID uuid) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        final IHyriPlayer hyriPlayer = new HyriPlayer(player.getName(), uuid);

        this.sendPlayer(hyriPlayer);

        return hyriPlayer;
    }

    @Override
    public void sendPlayer(IHyriPlayer player) {
        this.getJedis().set(this.getJedisKey(player.getUUID()), new Gson().toJson(player));
    }

    @Override
    public void removePlayer(UUID uuid) {
        this.getJedis().del(this.getJedisKey(uuid));
    }

    private String getJedisKey(UUID uuid) {
        return REDIS_KEY + uuid.toString();
    }

    private Jedis getJedis() {
        return this.plugin.getAPI().getJedisResource();
    }

    /**
     * In Game player management
     */

    @Override
    public void kickPlayer(UUID uuid, String reason) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.kickPlayer(reason);
        }
    }

    @Override
    public void connectPlayer(UUID uuid, String server) {
        this.api.getServerManager().sendPlayerToServer(uuid, server);
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

}
