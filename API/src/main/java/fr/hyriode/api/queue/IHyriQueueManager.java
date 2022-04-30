package fr.hyriode.api.queue;

import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 19:05
 */
public interface IHyriQueueManager {

    void addHandler(IHyriQueueHandler handler);

    void removeHandler(IHyriQueueHandler handler);

    boolean addPlayerInQueueWithPartyCheck(UUID playerId, String game, String gameType);

    void addPlayerInQueue(UUID playerId, String game, String gameType);

    void addPlayerInQueue(UUID playerId, String game, String gameType, String map);

    void removePlayerFromQueue(UUID playerId);

    void addPartyInQueue(IHyriParty party, String game, String gameType);

    void addPartyInQueue(IHyriParty party, String game, String gameType, String map);

    void removePartyFromQueue(UUID partyId);

    void updatePartyInQueue(IHyriParty party);

}
