package fr.hyriode.api.impl.server;

import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriServer implements IHyriServer {

    private final String name;
    private final String type;
    private final long startedTime;

    private State state;

    private final HyggdrasilManager hyggdrasilManager;

    public HyriServer(HyggdrasilManager hyggdrasilManager, String name, long startedTime) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.name = name;
        this.type = HyggServer.getTypeFromName(name);
        this.startedTime = startedTime;
        this.state = State.STARTING;
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
        return Bukkit.getOnlinePlayers().size();
    }

}
