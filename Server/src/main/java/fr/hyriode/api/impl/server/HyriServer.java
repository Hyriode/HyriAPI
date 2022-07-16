package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hystia.api.config.IConfig;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriServer implements IHyriServer {

    private final String name;
    private final String type;
    private final long startedTime;
    private final HyggData data;
    private int slots = -1;
    private boolean accessible;

    private IConfig config;

    private final String gameType;
    private String map;

    private State state;
    private Runnable stopHandler;

    private final HyggdrasilManager hyggdrasilManager;

    private final List<UUID> players;
    private final List<UUID> playersPlaying;

    public HyriServer(HyggdrasilManager hyggdrasilManager, String name, long startedTime, HyggData data) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.name = name;
        this.type = HyggServer.getTypeFromName(name);
        this.startedTime = startedTime;
        this.data = data;
        this.state = State.STARTING;
        this.gameType = data.get(HyggServer.SUB_TYPE_KEY);
        this.map = data.get(HyggServer.MAP_KEY);
        this.players = new ArrayList<>();
        this.playersPlaying = new ArrayList<>();
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

        HyriCommonImplementation.log("Changing state to " + this.state.name());

        this.setAccessible(this.state.isAccessible());
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
    public List<UUID> getPlayers() {
        return this.players;
    }

    @Override
    public void addPlayer(UUID player) {
        this.players.add(player);
    }

    @Override
    public void removePlayer(UUID player) {
        this.players.remove(player);
    }

    @Override
    public List<UUID> getPlayersPlaying() {
        return this.playersPlaying;
    }

    @Override
    public void addPlayerPlaying(UUID player) {
        this.playersPlaying.add(player);

        this.hyggdrasilManager.sendData();
    }

    @Override
    public void removePlayerPlaying(UUID player) {
        this.playersPlaying.remove(player);

        this.hyggdrasilManager.sendData();
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
    public String getSubType() {
        return this.gameType;
    }

    @Override
    public String getMap() {
        return this.map;
    }

    public void setMap(String map) {
        this.map = map;
        this.data.add(HyggServer.MAP_KEY, map);

        this.hyggdrasilManager.sendData();
    }

    @Override
    public HyggData getData() {
        return this.data;
    }

    @Override
    public void setAccessible(boolean accessible) {
        this.accessible = accessible;

        this.hyggdrasilManager.sendData();
    }

    @Override
    public boolean isAccessible() {
        return this.accessible;
    }

    @Override
    public double getTPS() {
        return MinecraftServer.getServer().recentTps[0];
    }

    @Override
    public <T extends IConfig> T getConfig(Class<T> configClass) {
        if (this.config != null) {
            return configClass.cast(this.config);
        }

        final T config = HyriAPI.get().getHystiaAPI().getConfigManager().getConfig(configClass, this.type, this.gameType, this.map);

        this.config = config;

        return config;
    }

}
