package fr.hyriode.hyriapi.implementation.api.server;

import fr.hyriode.hyggdrasilconnector.api.ServerState;
import fr.hyriode.hyriapi.server.Server;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 10:01
 */
public class HyriServer extends Server {

    private ServerState state;

    private int players;
    private int slots;

    private final long startedTime;

    private final String id;

    public HyriServer(String id, long startedTime, int slots, int players, ServerState state) {
        this.id = id;
        this.startedTime = startedTime;
        this.slots = slots;
        this.players = players;
        this.state = state;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public long getStartedTime() {
        return this.startedTime;
    }

    @Override
    public int getSlots() {
        return this.slots;
    }

    @Override
    public void setSlots(int slots) {
        this.slots = slots;
    }

    @Override
    public int getPlayers() {
        return this.players;
    }

    @Override
    public void setPlayers(int players) {
        this.players = players;
    }

    @Override
    public ServerState getState() {
        return this.state;
    }

    @Override
    public void setState(ServerState state) {
        this.state = state;
    }

}
