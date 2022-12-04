package fr.hyriode.api.queue;

import fr.hyriode.api.party.IHyriParty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 30/06/2022 at 10:40
 */
public interface IHyriQueue {

    /**
     * Get the identifier of the queue
     *
     * @return A {@linkplain UUID unique identifier}
     */
    UUID getId();

    /**
     * Get the type of the queue
     *
     * @return A {@link Type}
     */
    @NotNull Type getType();

    /**
     * Get the game of the queue
     *
     * @return A game name
     */
    @NotNull String getGame();

    /**
     * Get the type of the game
     *
     * @return A game type
     */
    @NotNull String getGameType();

    /**
     * Get the map of the game
     *
     * @return A map; can be null
     */
    @Nullable String getMap();

    /**
     * Get the server queued
     *
     * @return A server name. E.g. bedwars-csq15b
     */
    @Nullable String getServer();

    /**
     * Get the players in queue
     *
     * @return A list of player {@link UUID}
     */
    @NotNull Set<UUID> getPlayers();

    /**
     * Get all the players in queue.<br>
     * This method also calculate players in a {@linkplain IHyriParty party}.
     *
     * @return A list of player lists
     */
    @NotNull Set<Set<UUID>> getTotalPlayers();

    /** All the available types of a queue */
    enum Type {

        /** The queue is used for a game */
        GAME,
        /** The queue is used to connect to a server */
        SERVER

    }

}
