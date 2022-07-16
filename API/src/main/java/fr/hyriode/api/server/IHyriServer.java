package fr.hyriode.api.server;

import fr.hyriode.api.application.IHyriApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hystia.api.config.IConfig;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public interface IHyriServer extends IHyriApplication<IHyriServer.State> {

    /**
     * Get server type
     *
     * @return Server type
     */
    String getType();

    /**
     * Get server players
     *
     * @return Server players
     */
    List<UUID> getPlayers();

    /**
     * Add a player
     *
     * @param player The player to add
     */
    void addPlayer(UUID player);

    /**
     * Remove a player
     *
     * @param player The player to remove
     */
    void removePlayer(UUID player);

    /**
     * Get server players playing
     *
     * @return Server players
     */
    List<UUID> getPlayersPlaying();

    /**
     * Add a player playing
     *
     * @param player The player to add
     */
    void addPlayerPlaying(UUID player);

    /**
     * Remove a player playing
     *
     * @param player The player to remove
     */
    void removePlayerPlaying(UUID player);

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
     * Get the subtype of the server.<br>
     * This getter will only work if the server has a subtype!
     *
     * @return A game type
     */
    String getSubType();

    /**
     * Get the map to use on the server
     *
     * @return A map name
     */
    String getMap();

    /**
     * Get the data provided by Hyggdrasil
     *
     * @return A {@link HyggData} object
     */
    HyggData getData();

    /**
     * Set if the server is accessible to normal players
     *
     * @param accessible New value for server's accessibility
     */
    void setAccessible(boolean accessible);

    /**
     * Check if the server is accessible to normal players
     *
     * @return <code>true</code> if the server is accessible
     */
    boolean isAccessible();

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
     * @return A {@link IConfig} object
     */
    <T extends IConfig> T getConfig(Class<T> configClass);

    /**
     * The enumeration of all the states available for a server
     */
    enum State implements IHyriApplication.IState {

        /** Server is in creation */
        CREATING(false),
        /** Server is starting (onEnable in plugin) */
        STARTING(false),
        /** Server is ready to host players */
        READY(true),
        /** Server is playing a game */
        PLAYING(false),
        /** Server is stopping (onDisable in plugin) */
        SHUTDOWN(false),
        /** Server is idling (an error occurred or just freezing) */
        IDLE(false);

        /** Boolean that defines if the server can be joined by players or not */
        private final boolean accessible;

        /**
         * Constructor of {@link State}
         *
         * @param accessible The accessible state
         */
        State(boolean accessible) {
            this.accessible = accessible;
        }

        /**
         * Check if the server is accessible with this state
         *
         * @return <code>true</code>
         */
        public boolean isAccessible() {
            return this.accessible;
        }

    }

}
