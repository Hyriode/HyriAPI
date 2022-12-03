package fr.hyriode.api.impl.common.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.packet.BroadcastMessagePacket;
import fr.hyriode.api.chat.packet.PlayerMessagePacket;
import fr.hyriode.api.event.model.HyriAccountCreatedEvent;
import fr.hyriode.api.impl.common.player.nickname.HyriNicknameManager;
import fr.hyriode.api.impl.common.player.packet.PlayerKickPacket;
import fr.hyriode.api.impl.common.player.title.PlayerTitlePacket;
import fr.hyriode.api.impl.common.player.title.TitlePacket;
import fr.hyriode.api.impl.common.whitelist.HyriWhitelistManager;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.model.HyriSendPlayerPacket;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.nickname.IHyriNicknameManager;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.whitelist.IHyriWhitelistManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class CHyriPlayerManager implements IHyriPlayerManager {

    private static final Function<String, String> PLAYERS_KEY = uuid -> "players:" + uuid;
    private static final Function<String, String> PLAYERS_SESSIONS_KEY = uuid -> "players-sessions:" + uuid;
    private static final Function<String, String> IDS_KEY = name -> "uuid:" + name.toLowerCase();

    private final IHyriNicknameManager nicknameManager;
    private final IHyriWhitelistManager whitelistManager;

    public CHyriPlayerManager() {
        this.nicknameManager = new HyriNicknameManager();
        this.whitelistManager = new HyriWhitelistManager();
    }

    @Override
    public UUID getPlayerId(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String result = jedis.get(IDS_KEY.apply(name));

            return result != null ? UUID.fromString(result) : null;
        });
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
    public IHyriPlayer createPlayer(boolean premium, UUID uuid, String name) {
        final HyriPlayer player = new HyriPlayer(premium,  name, uuid);

        if (HyriAPI.get().getConfig().isDevEnvironment()) {
            final HyriRank rank = new HyriRank();

            rank.setStaffType(HyriStaffRankType.ADMINISTRATOR);

            player.setRank(rank);
        }

        player.update();

        this.setPlayerId(name, uuid);

        HyriAPI.get().getEventBus().publishAsync(new HyriAccountCreatedEvent(player));

        return player;
    }

    @Override
    public IHyriPlayer getPlayer(UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> HyriAPI.GSON.fromJson(jedis.get(PLAYERS_KEY.apply(uuid.toString())), HyriPlayer.class));
    }

    @Override
    public void updatePlayer(IHyriPlayer player) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PLAYERS_KEY.apply(player.getUniqueId().toString()), HyriAPI.GSON.toJson(player)));
    }

    @Override
    public void removePlayer(UUID uuid) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PLAYERS_KEY.apply(uuid.toString())));
    }

    @Override
    public List<IHyriPlayer> getPlayers() {
        final List<IHyriPlayer> players = new ArrayList<>();

        for (UUID playerId : this.getPlayersId()) {
            players.add(this.getPlayer(playerId));
        }
        return players;
    }

    @Override
    public List<UUID> getPlayersId() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<UUID> uuids = new ArrayList<>();

            for (String key : jedis.keys(PLAYERS_KEY.apply("*"))) {
                uuids.add(UUID.fromString(key.split(":")[1]));
            }
            return uuids;
        });
    }

    @Override
    public @Nullable IHyriPlayerSession getSession(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(PLAYERS_SESSIONS_KEY.apply(playerId.toString()));

            return json == null ? null : HyriAPI.GSON.fromJson(json, HyriPlayerSession.class);
        });
    }

    @Override
    public void updateSession(@NotNull IHyriPlayerSession session) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PLAYERS_SESSIONS_KEY.apply(session.getPlayerId().toString()), HyriAPI.GSON.toJson(session)));
    }

    @Override
    public void deleteSession(@NotNull UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PLAYERS_SESSIONS_KEY.apply(playerId.toString())));
    }

    @Override
    public List<IHyriPlayerSession> getSessions() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<IHyriPlayerSession> sessions = new ArrayList<>();

            for (String key : jedis.keys(PLAYERS_SESSIONS_KEY.apply("*"))) {
                sessions.add(HyriAPI.GSON.fromJson(jedis.get(key), HyriPlayerSession.class));
            }
            return sessions;
        });
    }

    @Override
    public void kickPlayer(UUID uuid, String component) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new PlayerKickPacket(uuid, component));
    }

    @Override
    public void connectPlayer(UUID uuid, String server) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new HyriSendPlayerPacket(uuid, server));
    }

    @Override
    public void broadcastMessage(String message, boolean component) {
        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new BroadcastMessagePacket(message, component));
    }

    @Override
    public void sendMessage(UUID uuid, String message, boolean component) {
        HyriAPI.get().getPubSub().send(HyriChannel.CHAT, new PlayerMessagePacket(uuid, message, component));
    }

    @Override
    public void sendTitle(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new PlayerTitlePacket(uuid, title, subtitle, fadeIn, stay, fadeOut));
    }

    @Override
    public void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new TitlePacket(title, subtitle, fadeIn, stay, fadeOut));
    }

    @Override
    public int getPing(UUID uuid) {
        return -1;
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
