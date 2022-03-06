package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.proxy.IHyriProxy;
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

    private final HyggdrasilManager hyggdrasilManager;

    public HyriProxy(HyggdrasilManager hyggdrasilManager, String name, long startedTime) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.name = name;
        this.startedTime = startedTime;
        this.state = State.STARTING;
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
    public int getPlayers() {
        return ProxyServer.getInstance().getOnlineCount();
    }

}
