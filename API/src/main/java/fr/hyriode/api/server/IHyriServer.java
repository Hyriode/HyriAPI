package fr.hyriode.api.server;

import fr.hyriode.api.application.IHyriApplication;
import fr.hyriode.api.config.IHyriConfig;
import fr.hyriode.api.host.HostData;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public interface IHyriServer extends IHyriApplication<HyggServer.State> {

    /**
     * Get server type
     *
     * @return Server type
     */
    String getType();

    /**
     * Get the type of the running game on the server
     *
     * @return A game type; or <code>null</code> if the server is not running a game
     */
    @Nullable
    String getGameType();

    /**
     * Get the map used on the server
     *
     * @return A map name
     */
    @Nullable
    String getMap();

    /**
     * Set the map used on the server
     *
     * @param map The new map name
     */
    void setMap(@Nullable String map);

    /**
     * Get the current accessibility level of the server
     *
     * @return A {@link HyggServer.Accessibility}
     */
    @NotNull
    HyggServer.Accessibility getAccessibility();

    /**
     * Set the current accessibility of the server
     *
     * @param accessibility The new {@link HyggServer.Accessibility}
     */
    void setAccessibility(@NotNull HyggServer.Accessibility accessibility);

    /**
     * Get the current type of process running on the server
     *
     * @return A {@link HyggServer.Process}
     */
    @NotNull
    HyggServer.Process getProcess();

    /**
     * Set the type of the process running on the server
     *
     * @param process The new {@link HyggServer.Process}
     */
    void setProcess(@NotNull HyggServer.Process process);

    /**
     * Get the data provided by Hyggdrasil
     *
     * @return A {@link HyggData} object
     */
    @NotNull
    HyggData getData();

    /**
     * Get the host data of the server.<br>
     * Warning: it will only work if the server is running in host mode
     *
     * @return A {@link HostData} object
     */
    HostData getHostData();

    /**
     * Set the host data of the server
     *
     * @param hostData The new {@link HostData}
     */
    void setHostData(HostData hostData);

    /**
     * Get server players
     *
     * @return Server players
     */
    @NotNull
    Set<UUID> getPlayers();

    /**
     * Add a player
     *
     * @param player The player to add
     */
    void addPlayer(@NotNull UUID player);

    /**
     * Remove a player
     *
     * @param player The player to remove
     */
    void removePlayer(@NotNull UUID player);

    /**
     * Get server players playing
     *
     * @return Server players
     */
    @NotNull
    Set<UUID> getPlayersPlaying();

    /**
     * Add a player playing
     *
     * @param player The player to add
     */
    void addPlayerPlaying(@NotNull UUID player);

    /**
     * Remove a player playing
     *
     * @param player The player to remove
     */
    void removePlayerPlaying(@NotNull UUID player);

    /**
     * Get the slots of the server
     *
     * @return An amount of maximum players
     */
    int getSlots();

    /**
     * Set the slots of the server
     *
     * @param slots New maximum amount of players
     */
    void setSlots(int slots);

    /**
     * Get the average tps of the server
     *
     * @return The ticks per second of the server
     */
    double getTPS();

    /**
     * Get the configuration of the server
     *
     * @param configClass The class of the config
     * @param <T> The type of the config
     * @return A {@link IHyriConfig} object
     */
    <T extends IHyriConfig> T getConfig(Class<T> configClass);

    /**
     * Reset the current configuration of the server.<br>
     * Useful when the map changed for example.
     */
    void resetConfig();

}
