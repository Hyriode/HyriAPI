package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.api.queue.packet.JoinQueuePacket;
import fr.hyriode.api.queue.packet.LeaveQueuePacket;
import fr.hyriode.api.queue.packet.QueuePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:18
 */
public class HyriQueueManager implements IHyriQueueManager {

    private static final String REDIS_KEY = "queues:";

    private final List<IHyriQueueHandler> handlers;

    public HyriQueueManager() {
        this.handlers = new ArrayList<>();

        HyriAPI.get().getNetworkManager().getEventBus().register(new QueueListener(this));
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
    public void addPlayerInQueue(@NotNull UUID playerId, @NotNull String game, @NotNull String gameType, String map) {
        this.sendQueuePacket(new JoinQueuePacket(playerId, IHyriQueue.Type.GAME, game, gameType, map, null));
    }

    @Override
    public void addPlayerInQueue(@NotNull UUID playerId, @NotNull String serverName) {
        this.sendQueuePacket(new JoinQueuePacket(playerId, IHyriQueue.Type.SERVER, null, null, null, serverName));
    }

    @Override
    public void removePlayerFromQueue(@NotNull UUID playerId) {
        this.sendQueuePacket(new LeaveQueuePacket(playerId));
    }

    @Override
    public @Nullable IHyriQueue getQueue(UUID queueId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(REDIS_KEY + queueId.toString());

            return json == null ? null : HyriAPI.GSON.fromJson(json, HyriQueue.class);
        });
    }

    @Override
    public void updateQueue(@NotNull IHyriQueue queue) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(REDIS_KEY + queue.getId().toString(), HyriAPI.GSON.toJson(queue)));
    }

    @Override
    public void deleteQueue(@NotNull UUID queueId) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(REDIS_KEY + queueId));
    }

    private void sendQueuePacket(QueuePacket packet) {
        HyriAPI.get().getPubSub().send(HyriChannel.QUEUES, packet);
    }

    public List<IHyriQueueHandler> getHandlers() {
        return this.handlers;
    }

}
