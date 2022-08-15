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
     * Add a player in a specific queue
     *
     * @param playerId The unique id of the player
     * @param game The game's name
     * @param gameType The game's type
     * @param map The game's map
     * @param partyCheck If <code>true</code>, and the player is in a party, it will add his party in queue
     * @return <code>true</code> if the request to add the player in the queue has been submitted
     */
    boolean addPlayerInQueue(UUID playerId, String game, String gameType, String map, boolean partyCheck);

    /**
     * Add a player in a specific queue
     *
     * @param playerId The unique id of the player
     * @param game The game's name
     * @param gameType The game's type
     * @param map The game's map
     * @return <code>true</code> if the request to add the player in the queue has been submitted
     */
    default boolean addPlayerInQueue(UUID playerId, String game, String gameType, String map) {
        return this.addPlayerInQueue(playerId, game, gameType, map, false);
    }

    /**
     * Add a player in a server queue
     *
     * @param playerId The unique id of the player
     * @param serverName The name of the server to queue for
     * @param partyCheck If <code>true</code>, and the player is in a party, it will add his party in queue
     * @return <code>true</code> if the request to add the player in the queue has been submitted
     */
    boolean addPlayerInQueue(UUID playerId, String serverName, boolean partyCheck);

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
     * @param map The game's map
     */
    void addPartyInQueue(IHyriParty party, String game, String gameType, String map);

    /**
     * Add a party in a queue
     *
     * @param party The party to add
     * @param game The game's name
     * @param gameType The game's type
     */
    default void addPartyInQueue(IHyriParty party, String game, String gameType) {
        this.addPartyInQueue(party, game, gameType, null);
    }

    /**
     * Add a party in a server queue
     *
     * @param party The party to add
     * @param serverName The name of the server to queue for
     */
    void addPartyInQueue(IHyriParty party, String serverName);

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
     * Set the current queue of a party
     *
     * @param partyId The identifier of the party
     * @param game The queued game
     * @param gameType The type of the game
     * @param map The map of the game
     */
    void setPartyQueue(UUID partyId, String game, String gameType, String map);

    /**
     * Remove the queue linked to a party
     *
     * @param partyId The identifier of the party
     */
    void removePartyQueue(UUID partyId);

    /**
     * Get in which queue a player is in
     *
     * @param playerId The identifier of the player
     * @return The {@linkplain IHyriQueue queue} or <code>null</code> if the player is not in a queue
     */
    IHyriQueue getPlayerQueue(UUID playerId);

    /**
     * Set the current queue of a player
     *
     * @param playerId The identifier of the player
     * @param game The queued game
     * @param gameType The type of the game
     * @param map The map of the game
     */
    void setPlayerQueue(UUID playerId, String game, String gameType, String map);

    /**
     * Remove the queue linked to a player
     *
     * @param playerId The identifier of a player
     */
    void removePlayerQueue(UUID playerId);

}
