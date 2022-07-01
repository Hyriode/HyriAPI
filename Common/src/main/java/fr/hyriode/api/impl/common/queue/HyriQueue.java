package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.queue.IHyriQueue;

/**
 * Created by AstFaster
 * on 30/06/2022 at 10:46
 */
public class HyriQueue implements IHyriQueue {

    private final String game;
    private final String gameType;
    private final String map;

    public HyriQueue(String game, String gameType, String map) {
        this.game = game;
        this.gameType = gameType;
        this.map = map;
    }

    @Override
    public String getGame() {
        return this.game;
    }

    @Override
    public String getGameType() {
        return this.gameType;
    }

    @Override
    public String getMap() {
        return this.map;
    }

}
