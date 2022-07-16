package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.hylios.api.queue.QueueAPI;
import fr.hyriode.hylios.api.queue.QueueGroup;
import fr.hyriode.hylios.api.queue.QueuePlayer;
import fr.hyriode.hylios.api.queue.packet.group.QueueAddGroupPacket;
import fr.hyriode.hylios.api.queue.packet.group.QueueRemoveGroupPacket;
import fr.hyriode.hylios.api.queue.packet.group.QueueUpdateGroupPacket;
import fr.hyriode.hylios.api.queue.packet.player.QueueAddPlayerPacket;
import fr.hyriode.hylios.api.queue.packet.player.QueueRemovePlayerPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:18
 */
public class HyriQueueManager implements IHyriQueueManager {

    private static final Function<UUID, String> PARTIES_KEY = id -> "queue:party:" + id.toString();
    private static final Function<UUID, String> PLAYERS_KEY = id -> "queue:player:" + id.toString();

    private final List<IHyriQueueHandler> handlers;

    public HyriQueueManager() {
        this.handlers = new ArrayList<>();
    }

    public void start() {
        HyriAPI.get().getPubSub().subscribe(QueueAPI.CHANNEL, new HyriQueueReceiver(this));
        HyriAPI.get().getNetworkManager().getEventBus().register(new HyriQueueListener(this));
    }

    @Override
    public void addHandler(IHyriQueueHandler handler) {
        this.handlers.add(handler);
    }

    @Override
    public void removeHandler(IHyriQueueHandler handler) {
        this.handlers.remove(handler);
    }

    @Override
    public boolean addPlayerInQueue(UUID playerId, String game, String gameType, String map, boolean partyCheck) {
        final IHyriPlayer account = IHyriPlayer.get(playerId);

        if (account.hasParty() && partyCheck) {
            final IHyriParty party = HyriAPI.get().getPartyManager().getPlayerParty(playerId);

            if (party.isLeader(playerId)) {
                this.addPartyInQueue(party, game, gameType);
                return true;
            }
            return false;
        }

        this.sendQueuePacket(new QueueAddPlayerPacket(new QueuePlayer(playerId, account.getPriority()), game, gameType, map));
        return true;
    }

    @Override
    public void removePlayerFromQueue(UUID playerId) {
        this.sendQueuePacket(new QueueRemovePlayerPacket(playerId));
    }

    @Override
    public void addPartyInQueue(IHyriParty party, String game, String gameType, String map) {
        this.sendQueuePacket(new QueueAddGroupPacket(game, gameType, map, this.createGroupFromParty(party)));
    }

    @Override
    public void removePartyFromQueue(UUID partyId) {
        this.sendQueuePacket(new QueueRemoveGroupPacket(partyId));
    }

    @Override
    public void updatePartyInQueue(IHyriParty party) {
        this.sendQueuePacket(new QueueUpdateGroupPacket(this.createGroupFromParty(party)));
    }

    @Override
    public void removePartyQueue(UUID partyId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PARTIES_KEY.apply(partyId)));
    }

    @Override
    public IHyriQueue getPartyQueue(UUID partyId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(PARTIES_KEY.apply(partyId));

            if (json == null) {
                return null;
            }
            return HyriAPI.GSON.fromJson(json, HyriQueue.class);
        });
    }

    @Override
    public void setPartyQueue(UUID partyId, String game, String gameType, String map) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PARTIES_KEY.apply(partyId), HyriAPI.GSON.toJson(new HyriQueue(game, gameType, map))));
    }

    @Override
    public void removePlayerQueue(UUID playerId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(PLAYERS_KEY.apply(playerId)));
    }

    @Override
    public IHyriQueue getPlayerQueue(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(PLAYERS_KEY.apply(playerId));

            if (json == null) {
                return null;
            }
            return HyriAPI.GSON.fromJson(json, HyriQueue.class);
        });
    }

    @Override
    public void setPlayerQueue(UUID playerId, String game, String gameType, String map) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(PLAYERS_KEY.apply(playerId), HyriAPI.GSON.toJson(new HyriQueue(game, gameType, map))));
    }

    private QueueGroup createGroupFromParty(IHyriParty party) {
        final UUID leader = party.getLeader();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final QueuePlayer queueLeader = new QueuePlayer(leader, playerManager.getPlayer(leader).getPriority());
        final List<QueuePlayer> players = new ArrayList<>();

        for (UUID member : party.getMembers().keySet()) {
            if (party.isLeader(member)) {
                continue;
            }

            players.add(new QueuePlayer(member, playerManager.getPlayer(member).getPriority()));
        }
        return new QueueGroup(party.getId(), queueLeader, players);
    }

    private void sendQueuePacket(HyriPacket packet) {
        HyriAPI.get().getPubSub().send(QueueAPI.CHANNEL, packet);
    }

    public List<IHyriQueueHandler> getHandlers() {
        return this.handlers;
    }

}
