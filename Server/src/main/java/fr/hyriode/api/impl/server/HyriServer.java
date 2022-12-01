package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.HyggServerOptions;
import fr.hyriode.hystia.api.config.IConfig;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriServer implements IHyriServer {

    private final String name;
    private final String type;

    private final String gameType;
    private String map;

    private HyggServer.Accessibility accessibility;
    private HyggServer.Process process;

    private HyggServer.State state;
    private final HyggServerOptions options;
    private final HyggData data;
    private HostData hostData;

    private final Set<UUID> players;
    private final Set<UUID> playingPlayers;
    private int slots;

    private final long startedTime;

    private IConfig config;

    public HyriServer(HyggApplication application) {
        if (application == null) {
            this.name = "dev-server";
            this.type = "dev";
            this.gameType = null;
            this.map = null;
            this.accessibility = HyggServer.Accessibility.PUBLIC;
            this.process = HyggServer.Process.TEMPORARY;
            this.state = HyggServer.State.STARTING;
            this.options = new HyggServerOptions();
            this.data = new HyggData();
            this.hostData = null;
            this.players = new HashSet<>();
            this.playingPlayers = new HashSet<>();
            this.slots = -1;
            this.startedTime = System.currentTimeMillis();
            return;
        }

        final HyggServer handle = HyriAPI.get().getServerManager().getServer(application.getName());

        this.name = handle.getName();
        this.type = handle.getType();
        this.gameType = handle.getGameType();
        this.map = handle.getMap();
        this.accessibility = handle.getAccessibility();
        this.process = handle.getProcess();
        this.state = HyggServer.State.STARTING;
        this.options = handle.getOptions();
        this.data = handle.getData();
        this.hostData = HyriAPI.get().getHostManager().getHostData(handle);
        this.players = handle.getPlayers();
        this.playingPlayers = handle.getPlayingPlayers();
        this.slots = handle.getSlots();
        this.startedTime = handle.getStartedTime();

        this.update();
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
    public @Nullable String getGameType() {
        return this.gameType;
    }

    @Override
    public @Nullable String getMap() {
        return this.map;
    }

    @Override
    public void setMap(@Nullable String map) {
        this.map = map;

        this.update();
    }

    @Override
    public @NotNull HyggServer.Accessibility getAccessibility() {
        return this.accessibility;
    }

    @Override
    public void setAccessibility(HyggServer.@NotNull Accessibility accessibility) {
        this.accessibility = accessibility;

        this.update();
    }

    @Override
    public @NotNull HyggServer.Process getProcess() {
        return this.process;
    }

    @Override
    public void setProcess(HyggServer.@NotNull Process process) {
        this.process = process;

        this.update();
    }

    @Override
    public HyggServer.State getState() {
        return this.state;
    }

    @Override
    public void setState(HyggServer.State state) {
        this.state = state;

        HyriAPI.get().log("State changed to " + state.name() + ".");

        this.update();
    }

    @Override
    public @NotNull HyggServerOptions getMinecraftOptions() {
        return this.options;
    }

    @Override
    public @NotNull HyggData getData() {
        return this.data;
    }

    @Override
    public HostData getHostData() {
        return this.hostData;
    }

    @Override
    public void setHostData(HostData hostData) {
        this.hostData = hostData;
    }

    @Override
    public @NotNull Set<UUID> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public void addPlayer(@NotNull UUID player) {
        this.playingPlayers.add(player);

        this.update();
    }

    @Override
    public void removePlayer(@NotNull UUID player) {
        this.playingPlayers.remove(player);

        this.update();
    }

    @Override
    public @NotNull Set<UUID> getPlayersPlaying() {
        return Collections.unmodifiableSet(this.playingPlayers);
    }

    @Override
    public void addPlayerPlaying(@NotNull UUID player) {
        this.playingPlayers.add(player);

        this.update();
    }

    @Override
    public void removePlayerPlaying(@NotNull UUID player) {
        this.playingPlayers.remove(player);

        this.update();
    }

    @Override
    public int getSlots() {
        return this.slots;
    }

    @Override
    public void setSlots(int slots) {
        this.slots = slots;

        this.update();
    }

    @Override
    public long getStartedTime() {
        return this.startedTime;
    }

    @Override
    public double getTPS() {
        return MinecraftServer.getServer().recentTps[0];
    }

    private void update() {
        HyriAPI.get().getHyggdrasilManager().sendData();
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

    @Override
    public void resetConfig() {
        this.config = null;
    }

}
