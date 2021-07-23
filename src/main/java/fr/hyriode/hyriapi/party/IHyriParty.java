package fr.hyriode.hyriapi.party;

import java.util.List;
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
     */
    void setLeader(UUID leader);

    /**
     * Get a list of all member {@link UUID}
     *
     * @return - A list of {@link UUID}
     */
    List<UUID> getMembers();

    /**
     * Add a member in the party by giving his {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     */
    void addMember(UUID uuid);

    /**
     * Remove a member of the party by giving his {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     */
    void removeMember(UUID uuid);

    /**
     * Get if the party contains a member with the given {@link UUID}
     *
     * @param uuid - Member {@link UUID}
     * @return - <code>true</code> if contains member
     */
    boolean hasMember(UUID uuid);

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
     */
    void setServer(String server);

}
