package fr.hyriode.api.impl.common.player;

import com.google.gson.JsonElement;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.packet.PlayerComponentPacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.event.model.HyriAccountCreatedEvent;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.impl.common.player.nickname.HyriNicknameManager;
import fr.hyriode.api.impl.common.player.title.PlayerTitlePacket;
import fr.hyriode.api.impl.common.player.title.TitlePacket;
import fr.hyriode.api.impl.common.whitelist.HyriWhitelistManager;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.model.HyriSendPlayerPacket;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.nickname.IHyriNicknameManager;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.whitelist.IHyriWhitelistManager;
import fr.hyriode.hydrion.client.module.PlayerModule;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public abstract class HyriCPlayerManager implements IHyriPlayerManager {

    private static final Function<UUID, String> PLAYERS_KEY = uuid -> "players:" + uuid.toString();
    private static final Function<String, String> IDS_KEY = name -> "uuid:" + name.toLowerCase();

    private final IHyriNicknameManager nicknameManager;
    private final IHyriWhitelistManager whitelistManager;

    protected final HydrionManager hydrionManager;
    protected PlayerModule playerModule;

    public HyriCPlayerManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;
        this.nicknameManager = new HyriNicknameManager();
        this.whitelistManager = new HyriWhitelistManager();

        if (this.hydrionManager.isEnabled()) {
            this.playerModule = this.hydrionManager.getClient().getPlayerModule();
        }
    }

    @Override
    public UUID getPlayerId(String name, boolean allowHydrionCheck) {
        final UUID playerId = HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String result = jedis.get(IDS_KEY.apply(name));

            return result != null ? UUID.fromString(result) : null;
        });

        if (playerId != null) {
            return playerId;
        }

        if (!this.hydrionManager.isEnabled() || !allowHydrionCheck) {
            return null;
        }

        try {
            return this.hydrionManager.getClient().getUUIDModule().getUUID(name).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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
    public IHyriPlayer getPlayer(UUID uuid) {
        final IHyriPlayer player = this.getPlayerFromRedis(uuid);

        return player != null ? player : this.getPlayerFromHydrion(uuid);
    }

    @Override
    public IHyriPlayer getPlayerFromRedis(UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(PLAYERS_KEY.apply(uuid));

            if (json != null) {
                return this.deserialize(json);
            }
            return null;
        });
    }

    @Override
    public IHyriPlayer getPlayerFromRedis(String name) {
        final UUID playerId = this.getPlayerId(name, false);

        if (playerId != null) {
            return this.getPlayerFromRedis(playerId);
        }
        return null;
    }

    @Override
    public IHyriPlayer getPlayerFromHydrion(UUID uuid) {
        if (this.hydrionManager.isEnabled()) {
            try {
                return this.playerModule.getPlayer(uuid).thenApply(response -> {
                    final JsonElement content = response.getContent();

                    if (!content.isJsonNull()) {
                        return this.deserialize(content.toString());
                    }
                    return null;
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
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
            final HyriRank rank = new HyriRank(HyriPlayerRankType.PLAYER);

            rank.setStaffType(HyriStaffRankType.ADMINISTRATOR);

            player.setRank(rank);
        }

        player.update();

        this.setPlayerId(name, uuid);

        HyriAPI.get().getEventBus().publishAsync(new HyriAccountCreatedEvent(player));

        return player;
    }

    @Override
    public synchronized void sendPlayer(IHyriPlayer player) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PLAYERS_KEY.apply(player.getUniqueId()), HyriAPI.GSON.toJson(player)));
    }

    @Override
    public void removePlayer(UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PLAYERS_KEY.apply(uuid)));
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

    private HyriPlayer deserialize(String json) {
        return HyriAPI.GSON.fromJson(json, HyriPlayer.class);
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
