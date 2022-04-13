package fr.hyriode.api.party;

import fr.hyriode.api.HyriAPI;

import java.util.Date;
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
     * @param server - New server id
     */
    void setServer(String server);

    /**
     * Send message to all members of the party
     *
     * @param channel - Channel to send
     * @param message - Message to send
     * @param sender - Sender of the message
     */
    default void sendMessage(String channel, String message, UUID sender) {
        this.sendMessage(channel, message, sender, false);
    }

    /**
     * Send message to all members of the party
     *
     * @param channel - Channel to send
     * @param message - Message to send
     * @param sender - Sender of the message
     * @param force - <code>true</code> to bypass checks
     */
    void sendMessage(String channel, String message, UUID sender, boolean force);

    /**
     * Disband the party
     *
     * @param reason The reason of why the party need to be disbanded
     */
    void disband(HyriPartyDisbandReason reason);

    /**
     * Update a player in Redis
     */
    default void update() {
        HyriAPI.get().getPartyManager().sendParty(this);
    }

}
