package fr.hyriode.api.party;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.Map;
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
     * @param uuid - Party id
     * @return - {@link IHyriParty}
     */
    IHyriParty getParty(UUID uuid);

    /**
     * Get the party of a player by giving his {@link UUID}
     *
     * @param playerUUID - Player {@link UUID}
     * @return - {@link IHyriParty}
     */
    IHyriParty getPlayerParty(UUID playerUUID);

    /**
     * Get a new party with the id of the leader
     *
     * @param leader - Leader {@link UUID}
     * @return - {@link IHyriParty}
     */
    IHyriParty createParty(UUID leader);

    /**
     * Send a party in Redis cache
     *
     * @param party - {@link IHyriPlayer}
     */
    void sendParty(IHyriParty party);

    /**
     * Remove a party with a given {@link UUID}
     *
     * @param uuid - Party {@link UUID}
     */
    void removeParty(UUID uuid);

    /**
     * Get the current server of a party by giving his id
     *
     * @param uuid - Party id
     * @return - Current party server
     */
    String getPartyServer(UUID uuid);

    /**
     * Get the current leader of a party by giving his id
     *
     * @param uuid - Party id
     * @return - Current leader id
     */
    UUID getPartyLeader(UUID uuid);

    /**
     * Get the list of members at a party by giving his id
     *
     * @param uuid - Party id
     * @return - A list of member {@link UUID} with his rank
     */
    Map<UUID, HyriPartyRank> getMembersInParty(UUID uuid);

}
