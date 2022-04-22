package fr.hyriode.api.party.event;

import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 09:04
 */
public class HyriPartyLeaderEvent extends HyriPartyPromoteEvent {

    /** The old leader of the party */
    private final UUID oldLeader;

    /**
     * Constructor of {@link HyriPartyPromoteEvent}
     *  @param party   The {@link IHyriParty} object
     * @param member  The {@link UUID} of the member
     * @param oldRank A {@link HyriPartyRank}
     * @param newRank A {@link HyriPartyRank}
     * @param oldLeader The {@link UUID} of the old leader
     */
    public HyriPartyLeaderEvent(IHyriParty party, UUID member, HyriPartyRank oldRank, HyriPartyRank newRank, UUID oldLeader) {
        super(party, member, oldRank, newRank);
        this.oldLeader = oldLeader;
    }

    /**
     * Get the old leader of the party
     *
     * @return A player {@link UUID}
     */
    public UUID getOldLeader() {
        return this.oldLeader;
    }

    /**
     * Get the new leader of the party
     *
     * @return A player {@link UUID}
     */
    public UUID getNewLeader() {
        return this.member;
    }

}
