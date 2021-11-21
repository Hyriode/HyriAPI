package fr.hyriode.hyriapi.impl.server;

import fr.hyriode.hyriapi.server.IHyriServer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriServer implements IHyriServer {

    private final int players;
    private final long startedTime;

    private final String name;
    private final String type;

    public HyriServer(String name, String type, long startedTime, int players) {
        this.name = name;
        this.type = type;
        this.startedTime = startedTime;
        this.players = players;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public long getStartedTime() {
        return this.startedTime;
    }

    @Override
    public int getPlayers() {
        return this.players;
    }


}
