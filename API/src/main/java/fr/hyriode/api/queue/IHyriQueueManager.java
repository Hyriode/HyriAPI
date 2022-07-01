package fr.hyriode.api.queue;

import fr.hyriode.api.party.IHyriParty;

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
     * Add a player in the queue but also check if the player has a party
     *
     * @param playerId The player's id
     * @param game The game's name
     * @param gameType The game's type
     * @return <code>true</code> if the player has been added in queue
     */
    boolean addPlayerInQueueWithPartyCheck(UUID playerId, String game, String gameType);

    /**
     * Add a player in a specific queue
     *
     * @param playerId The unique id of the player
     * @param game The game's name
     * @param gameType The game's type
     */
    void addPlayerInQueue(UUID playerId, String game, String gameType);

    /**
     * Add a player in a specific queue
     *
     * @param playerId The unique id of the player
     * @param game The game's name
     * @param gameType The game's type
     * @param map The game's map
     */
    void addPlayerInQueue(UUID playerId, String game, String gameType, String map);

    /**
     * Remove a player from the queue.<br>
     * This method will only work if the player is in a queue
     *
     * @param playerId The unique id of the player
     */
    void removePlayerFromQueue(UUID playerId);

    /**
     * Add a party in a queue
     *
     * @param party The party to add
     * @param game The game's name
     * @param gameType The game's type
     */
    void addPartyInQueue(IHyriParty party, String game, String gameType);

    /**
     * Add a party in a queue
     *
     * @param party The party to add
     * @param game The game's name
     * @param gameType The game's type
     * @param map The game's map
     */
    void addPartyInQueue(IHyriParty party, String game, String gameType, String map);

    /**
     * Remove a party from queue
     *
     * @param partyId The unique id of the party
     */
    void removePartyFromQueue(UUID partyId);

    /**
     * Update a party in queue.<br>
     * It will attach new players and remove old ones
     *
     * @param party The party to update in queue
     */
    void updatePartyInQueue(IHyriParty party);

    /**
     * Get in which queue a party is in
     *
     * @param partyId The identifier of the party
     * @return The {@linkplain IHyriQueue queue} or <code>null</code> if the party is not in a queue
     */
    IHyriQueue getPartyQueue(UUID partyId);

    /**
     * Get in which queue a player is in
     *
     * @param playerId The identifier of the player
     * @return The {@linkplain IHyriQueue queue} or <code>null</code> if the player is not in a queue
     */
    IHyriQueue getPlayerQueue(UUID playerId);

}
