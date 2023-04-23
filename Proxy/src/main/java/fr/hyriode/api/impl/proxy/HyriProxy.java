package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:59
 */
public class HyriProxy implements IHyriProxy {

    private final String name;

    private final HyggData data;
    private HyggProxy.State state;
    private final Set<UUID> players;

    private final long startedTime;

    public HyriProxy(HyggApplication application) {
        if (HyriAPI.get().getConfig().isDevEnvironment()) {
            this.name = "dev-proxy";
            this.data = new HyggData();
            this.state = HyggProxy.State.STARTING;
            this.players = new HashSet<>();
            this.startedTime = System.currentTimeMillis();
            return;
        }

        final HyggProxy info = HyriAPI.get().getProxyManager().getProxy(application.getName());

        this.name = info.getName();
        this.data = info.getData();
        this.state = HyggProxy.State.STARTING;
        this.players = info.getPlayers();
        this.startedTime = info.getStartedTime();
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
    @NotNull
    public HyggProxy.State getState() {
        return this.state;
    }

    @Override
    public void setState(@NotNull HyggProxy.State state) {
        this.state = state;

        HyriAPI.get().log("Changing state to " + this.state.name());

        this.update();
    }

    @Override
    public @NotNull Set<UUID> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public void addPlayer(@NotNull UUID player) {
        if (this.players.add(player)) {
            this.update();
        }
    }

    @Override
    public void removePlayer(@NotNull UUID player) {
        if (this.players.remove(player)) {
            this.update();
        }
    }

    @Override
    @NotNull
    public HyggData getData() {
        return this.data;
    }

    private void update() {
        HyriAPI.get().getHyggdrasilManager().sendData();
    }

}
