package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    private final Set<UUID> players;

    private final HyggdrasilManager hyggdrasilManager;

    private final HyggData data;

    public HyriProxy(HyggdrasilManager hyggdrasilManager, String name, long startedTime, HyggData data) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.name = name;
        this.players = new HashSet<>();
        this.startedTime = startedTime;
        this.state = State.STARTING;
        this.data = data;
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

        HyriCommonImplementation.log("Changing state to " + this.state.name());

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
        return this.players.size();
    }

    public void addPlayer(UUID player) {
        this.players.add(player);
    }

    public void removePlayer(UUID player) {
        this.players.remove(player);
    }

    @Override
    public HyggData getData() {
        return this.data;
    }

}
