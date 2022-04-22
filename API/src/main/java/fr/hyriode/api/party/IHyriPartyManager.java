package fr.hyriode.api.party;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 17:40
 */
public interface IHyriPartyManager {

    String REDIS_CHANNEL = "parties";

    /**
     * Get a party by giving his id
     *
     * @param uuid Party id
     * @return {@link IHyriParty}
     */
    IHyriParty getParty(UUID uuid);

    /**
     * Get a new party with the id of the leader
     *
     * @param leader Leader {@link UUID}
     * @return {@link IHyriParty}
     */
    IHyriParty createParty(UUID leader);

    /**
     * Send a party in Redis cache
     *
     * @param party {@link IHyriPlayer}
     */
    void sendParty(IHyriParty party);

    /**
     * Remove a party with a given {@link UUID}.<br>
     * Warning: this method doesn't call {@link fr.hyriode.api.party.event.HyriPartyDisbandEvent}. If you want to trigger it, please refer to {@link IHyriParty#disband(HyriPartyDisbandReason)}
     *
     * @param uuid Party {@link UUID}
     */
    void removeParty(UUID uuid);

    /**
     * Send a party's invitation to a given player
     *
     * @param partyId The identifier of the party
     * @param sender The sender of the invitation
     * @param player The player that will receive the invitation
     */
    void sendInvitation(UUID partyId, UUID sender, UUID player);

    /**
     * Remove a party's invitation from a given player
     *
     * @param partyId The identifier of the party
     * @param playerId The {@link UUID} of the player
     */
    void removeInvitation(UUID partyId, UUID playerId);

    /**
     * Check if a player has an invitation of a party
     *
     * @param partyId The identifier of the party
     * @param playerId The {@link UUID} of the player
     * @return <code>true</code> if the player has an invitation from the party
     */
    boolean hasInvitation(UUID partyId, UUID playerId);

    /**
     * Get all the invitations of a player
     *
     * @param player The {@link UUID} of the player
     * @return A list of {@link HyriPartyInvitation}
     */
    List<HyriPartyInvitation> getInvitations(UUID player);

    /**
     * Get the party of a player by giving his {@link UUID}
     *
     * @param playerUUID Player {@link UUID}
     * @return {@link IHyriParty}
     */
    IHyriParty getPlayerParty(UUID playerUUID);

    /**
     * Get the current server of a party by giving his id
     *
     * @param uuid - Party id
     * @return Current party server
     */
    String getPartyServer(UUID uuid);

    /**
     * Get the current leader of a party by giving his id
     *
     * @param uuid - Party id
     * @return Current leader id
     */
    UUID getPartyLeader(UUID uuid);

    /**
     * Get the list of members at a party by giving his id
     *
     * @param uuid Party id
     * @return A list of member {@link UUID} with his rank
     */
    Map<UUID, HyriPartyRank> getMembersInParty(UUID uuid);

}
