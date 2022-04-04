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
     * @return - Party {@link UUID}
     */
    UUID getId();

    /**
     * Get the {@link UUID} of the party leader
     *
     * @return - Leader {@link UUID}
     */
    UUID getLeader();

    /**
     * Set the leader of party by giving his {@link UUID}
     *
     * @param leader - New leader {@link UUID}
     * @return - This {@link IHyriParty} instance
     */
    IHyriParty setLeader(UUID leader);

    /**
     * Get the creation date of the party
     *
     * @return - Creation date
     */
    Date getCreationDate();

    /**
     * Get a list of all member {@link UUID}
     *
     * @return - A list of {@link UUID}
     */
    Map<UUID, HyriPartyRank> getMembers();

    /**
     * Add a member in the party by giving his {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     * @param rank - Member rank
     * @return - This {@link IHyriParty} instance
     */
    IHyriParty addMember(UUID uuid, HyriPartyRank rank);

    /**
     * Promote a member of the party by giving his {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     * @return - The new rank
     */
    HyriPartyRank promoteMember(UUID uuid);

    /**
     * Demote a member of the party by giving his {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     * @return - The new rank
     */
    HyriPartyRank demoteMember(UUID uuid);

    /**
     * Remove a member of the party by giving his {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     * @return - This {@link IHyriParty} instance
     */
    IHyriParty removeMember(UUID uuid);

    /**
     * Get if the party contains a member with the given {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     * @return - <code>true</code> if contains member
     */
    boolean hasMember(UUID uuid);

    /**
     * Check if the party is private
     * @return - <code>true</code> if private
     */
    boolean isPrivate();

    /**
     * Set the party as private
     * @param privateParty - <code>true</code> if private
     * @return - This {@link IHyriParty} instance
     */
    IHyriParty setPrivate(boolean privateParty);

    /**
     * Get the current server of the party
     *
     * @return - A server id
     */
    String getServer();

    /**
     * Set the current server of the party
     *
     * @param server - New server id
     * @return - This {@link IHyriParty} instance
     */
    IHyriParty setServer(String server);

    /**
     * Send message to all members of the party
     * @param channel - Channel to send
     * @param message - Message to send
     * @param sender - Sender of the message
     */
    default void sendMessage(String channel, String message, UUID sender) {
        this.sendMessage(channel, message, sender, false);
    }

    /**
     * Send message to all members of the party
     * @param channel - Channel to send
     * @param message - Message to send
     * @param sender - Sender of the message
     * @param force - <code>true</code> to bypass checks
     */
    void sendMessage(String channel, String message, UUID sender, boolean force);

    /**
     * Update a player in Redis
     */
    default void update() {
        HyriAPI.get().getPartyManager().sendParty(this);
    }
}
