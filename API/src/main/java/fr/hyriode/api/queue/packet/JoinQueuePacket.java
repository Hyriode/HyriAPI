package fr.hyriode.api.queue.packet;

import fr.hyriode.api.queue.IHyriQueue;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 15:18.<br>
 *
 * Packet used to add a player is a given queue.
 */
public class JoinQueuePacket extends QueuePacket {

    private final UUID playerId;
    private final IHyriQueue.Type queueType;
    private final String game;
    private final String gameType;
    private final String map;
    private final String server;

    public JoinQueuePacket(UUID playerId, IHyriQueue.Type queueType, String game, String gameType, String map, String server) {
        this.playerId = playerId;
        this.queueType = queueType;
        this.game = game;
        this.gameType = gameType;
        this.map = map;
        this.server = server;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public IHyriQueue.Type getQueueType() {
        return this.queueType;
    }

    public String getGame() {
        return this.game;
    }

    public String getGameType() {
        return this.gameType;
    }

    public String getMap() {
        return this.map;
    }

    public String getServer() {
        return this.server;
    }

}
