package fr.hyriode.api.impl.common.player;

import com.google.gson.JsonElement;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.HyriDefaultChatChannel;
import fr.hyriode.api.event.model.HyriAccountCreatedEvent;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
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

    protected final HydrionManager hydrionManager;
    protected PlayerModule playerModule;

    public HyriCPlayerManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;

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
        final IHyriPlayer player = HyriAPI.get().getRedisProcessor().get(jedis -> this.deserialize(jedis.get(PLAYERS_KEY.apply(uuid))));

        if (player != null) {
            return player;
        }

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
    public void sendPlayer(IHyriPlayer player) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PLAYERS_KEY.apply(player.getUniqueId()), HyriAPI.GSON.toJson(player)));
    }

    @Override
    public void removePlayer(UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PLAYERS_KEY.apply(uuid)));
    }

    @Override
    public void connectPlayer(UUID uuid, String server) {
        HyriAPI.get().getServerManager().sendPlayerToServer(uuid, server);
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        HyriAPI.get().getChatChannelManager().sendMessageToPlayer(HyriDefaultChatChannel.PLUGIN.getChannel(), message, uuid, true);
    }

    private HyriPlayer deserialize(String json) {
        return HyriAPI.GSON.fromJson(json, HyriPlayer.class);
    }

}
