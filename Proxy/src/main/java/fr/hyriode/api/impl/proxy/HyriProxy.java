package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import net.md_5.bungee.api.ProxyServer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:59
 */
public class HyriProxy implements IHyriProxy {

    private final String name;
    private final long startedTime;

    private State state;
    private Runnable stopHandler;

    private final HyggdrasilManager hyggdrasilManager;

    private final HyggData data;

    private int players;

    public HyriProxy(HyggdrasilManager hyggdrasilManager, String name, long startedTime, HyggData data) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.name = name;
        this.startedTime = startedTime;
        this.state = State.STARTING;
        this.data = data;
        this.players = ProxyServer.getInstance().getOnlineCount();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getStartedTime() {
        return this.startedTime;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public void setState(State state) {
        this.state = state;

        this.hyggdrasilManager.sendData();
    }

    @Override
    public Runnable getStopHandler() {
        return this.stopHandler;
    }

    @Override
    public void setStopHandler(Runnable stopHandler) {
        this.stopHandler = stopHandler;
    }

    @Override
    public int getPlayers() {
        return this.players;
    }

    @Override
    public void addPlayer() {
        this.players++;
    }

    @Override
    public void removePlayer() {
        this.players--;
    }

    @Override
    public HyggData getData() {
        return this.data;
    }

}
