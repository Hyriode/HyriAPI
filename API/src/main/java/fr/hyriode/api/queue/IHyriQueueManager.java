package fr.hyriode.api.queue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 19:05
 */
public interface IHyriQueueManager {

    /**
     * Add a queue handler
     *
     * @param handler The {@link IHyriQueueHandler} to add
     */
    void addHandler(IHyriQueueHandler handler);

    /**
     * Remove a queue handler
     *
     * @param handler The {@link IHyriQueueHandler} to remove
     */
    void removeHandler(IHyriQueueHandler handler);

    /**
     * Add a player in queue (for a game)
     *
     * @param playerId The {@link UUID} of the player to add
     * @param game The game to queue
     * @param gameType The type of the game to queue
     * @param map The map of the game to queue (optional)
     */
    void addPlayerInQueue(@NotNull UUID playerId, @NotNull String game, @NotNull String gameType, String map);

    /**
     * Add a player in queue (for a specified server)
     *
     * @param playerId The {@link UUID} of the player to add
     * @param serverName The name of the server to queue for
     */
    void addPlayerInQueue(@NotNull UUID playerId, @NotNull String serverName);

    /**
     * Remove a player from queue
     *
     * @param playerId The {@link UUID} of the player to remove
     */
    void removePlayerFromQueue(@NotNull UUID playerId);

    /**
     * Get a queue by giving its id
     *
     * @param queueId The {@link UUID} of the queue
     * @return The found {@link IHyriQueue}
     */
    @Nullable IHyriQueue getQueue(UUID queueId);

    /**
     * Get all the registered queues
     *
     * @return A list of {@link IHyriQueue}
     */
    @NotNull List<IHyriQueue> getQueues();

    /**
     * Update a queue in cache
     *
     * @param queue The {@link IHyriQueue} to update
     */
    void updateQueue(@NotNull IHyriQueue queue);

    /**
     * Delete a queue from cache by giving its id
     *
     * @param queueId The {@link UUID} of the queue
     */
    void deleteQueue(@NotNull UUID queueId);

}
