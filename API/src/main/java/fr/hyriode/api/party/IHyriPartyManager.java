package fr.hyriode.api.party;

import fr.hyriode.api.party.event.HyriPartyDisbandEvent;
import fr.hyriode.api.player.IHyriPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 17:40
 */
public interface IHyriPartyManager {

    /**
     * Get a party by giving his id
     *
     * @param uuid Party id
     * @return {@link IHyriParty}
     */
    IHyriParty getParty(@NotNull UUID uuid);

    /**
     * Get a new party with the id of the leader
     *
     * @param leader Leader {@link UUID}
     * @return {@link IHyriParty}
     */
    IHyriParty createParty(@NotNull UUID leader);

    /**
     * Send a party in Redis cache
     *
     * @param party {@link IHyriPlayer}
     */
    void updateParty(@NotNull IHyriParty party);

    /**
     * Remove a party with a given {@link UUID}.<br>
     * Warning: this method doesn't call {@link HyriPartyDisbandEvent}. If you want to trigger it, please refer to {@link IHyriParty#disband(IHyriParty.DisbandReason)}
     *
     * @param uuid Party {@link UUID}
     */
    void removeParty(@NotNull UUID uuid);

    /**
     * Send a party's request to a given player
     *
     * @param partyId The identifier of the party
     * @param sender The sender of the request
     * @param target The player that will receive the request
     */
    void sendRequest(UUID partyId, UUID sender, UUID target);

    /**
     * Remove a party's request from a given player
     *
     * @param partyId The identifier of the party
     * @param playerId The {@link UUID} of the player
     */
    void removeRequest(UUID partyId, UUID playerId);

    /**
     * Check if a player has a request of a party
     *
     * @param partyId The identifier of the party
     * @param playerId The {@link UUID} of the player
     * @return <code>true</code> if the player has a request from the party
     */
    boolean hasRequest(UUID partyId, UUID playerId);

    /**
     * Get all the requests of a player
     *
     * @param player The {@link UUID} of the player
     * @return A list of {@link HyriPartyRequest}
     */
    List<HyriPartyRequest> getRequests(UUID player);

    /**
     * Get the party of a player by giving his {@link UUID}
     *
     * @param playerUUID Player {@link UUID}
     * @return {@link IHyriParty}
     */
    IHyriParty getPlayerParty(UUID playerUUID);

}
