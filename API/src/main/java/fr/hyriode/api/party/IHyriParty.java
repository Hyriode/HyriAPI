package fr.hyriode.api.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.queue.IHyriQueue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 17:30
 */
public interface IHyriParty {

    /**
     * Get the id of the party
     *
     * @return Party {@link UUID}
     */
    UUID getId();

    /**
     * Get the {@link UUID} of the party leader
     *
     * @return Leader {@link UUID}
     */
    UUID getLeader();

    /**
     * Set the leader of party by giving his {@link UUID}
     *
     * @param leader - New leader {@link UUID}
     */
    void setLeader(UUID leader);

    /**
     * Check if a player is the leader of the party
     *
     * @param player The player {@link UUID}
     * @return <code>true</code> if the player is the leader of the party
     */
    boolean isLeader(UUID player);

    /**
     * Get the creation date of the party
     *
     * @return Creation date
     */
    Date getCreationDate();

    /**
     * Get a list of all member {@link UUID}
     *
     * @return A list of {@link UUID}
     */
    Map<UUID, HyriPartyRank> getMembers();

    /**
     * Get all the members of the party with a given rank
     *
     * @param rank The rank of the members to find
     * @return A list of members
     */
    List<UUID> getMembers(HyriPartyRank rank);

    /**
     * Invite a given player in the party
     *
     * @param sender The {@link UUID} of the invitation's sender
     * @param uuid The {@link UUID} of the player to invite
     */
    void invitePlayer(UUID sender, UUID uuid);

    /**
     * Add a member in the party by giving his {@link UUID}
     *
     * @param uuid Member {@link UUID}
     * @param rank Member rank
     */
    void addMember(UUID uuid, HyriPartyRank rank);

    /**
     * Promote a member of the party by giving his {@link UUID}
     *
     * @param uuid Member {@link UUID}
     * @return The new rank
     */
    HyriPartyRank promoteMember(UUID uuid);

    /**
     * Demote a member of the party by giving his {@link UUID}
     *
     * @param uuid Member {@link UUID}
     * @return The new rank
     */
    HyriPartyRank demoteMember(UUID uuid);

    /**
     * Remove a member of the party by giving his {@link UUID}
     *
     * @param uuid Member {@link UUID}
     */
    void removeMember(UUID uuid);

    /**
     * Kick a player from the party
     *
     * @param uuid The player to kick
     * @param kicker The player that kicked the member from the party
     */
    void kickMember(UUID uuid, UUID kicker);

    /**
     * Get if the party contains a member with the given {@link UUID}
     *
     * @param uuid Member {@link UUID}
     * @return <code>true</code> if contains member
     */
    boolean hasMember(UUID uuid);

    /**
     * Get the party's rank of a given player
     *
     * @param player The player {@link UUID}
     * @return The {@link HyriPartyRank} of the player-
     */
    HyriPartyRank getRank(UUID player);

    /**
     * Check if the party is private
     *
     * @return <code>true</code> if private
     */
    boolean isPrivate();

    /**
     * Set the party as private
     *
     * @param value <code>true</code> if private
     */
    void setPrivate(boolean value);

    /**
     * Get the current server of the party
     *
     * @return A server id
     */
    String getServer();

    /**
     * Set the current server of the party
     *
     * @param server New server id
     */
    void setServer(String server);

    /**
     * Check if the party chat is enabled for party members
     *
     * @return <code>true</code> if yes
     */
    boolean isChatEnabled();

    /**
     * Set if the party chat is enabled or not
     *
     * @param chatEnabled New value for enabling the chat or not
     */
    void setChatEnabled(boolean chatEnabled);

    /**
     * Send a message to all the members of the party
     *
     * @param sender The sender of the message
     * @param message The message to send
     */
    void sendMessage(UUID sender, String message);

    /**
     * Send a message component to all the members of the party
     *
     * @param component The component as json
     */
    void sendComponent(String component);

    /**
     * Disband the party
     *
     * @param reason The reason of why the party need to be disbanded
     */
    void disband(HyriPartyDisbandReason reason);

    /**
     * Warp a party on a given server
     *
     * @param server The server where the party will be sent
     */
    void warp(String server);

    /**
     * Get the current queue of the party.<br>
     * It can return <code>null</code> if the party is not in a queue
     *
     * @return A {@link IHyriQueue} object
     */
    IHyriQueue getQueue();

    /**
     * Check if the party is in a queue
     *
     * @return <code>true</code> if yes
     */
    default boolean hasQueue() {
        return this.getQueue() != null;
    }

    /**
     * Set the queue of the party
     *
     * @param game The game
     * @param gameType The game type
     * @param map The map used for the game
     */
    void setQueue(String game, String gameType, String map);

    /**
     * Update a player in Redis
     */
    default void update() {
        HyriAPI.get().getPartyManager().sendParty(this);
    }

}
