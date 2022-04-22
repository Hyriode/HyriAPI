package fr.hyriode.api.party.event;

import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:52
 */
public abstract class HyriPartyRankEvent extends HyriPartyMemberEvent {

    /** The old rank of the player */
    protected final HyriPartyRank oldRank;
    /** The new rank of the player */
    protected final HyriPartyRank newRank;

    /**
     * Constructor of {@link HyriPartyEvent}
     *  @param party  The {@link IHyriParty} object
     * @param member The {@link UUID} of the member
     * @param oldRank A {@link HyriPartyRank}
     * @param newRank A {@link HyriPartyRank}
     */
    public HyriPartyRankEvent(IHyriParty party, UUID member, HyriPartyRank oldRank, HyriPartyRank newRank) {
        super(party, member);
        this.oldRank = oldRank;
        this.newRank = newRank;
    }

    /**
     * Get the old rank of the party member
     *
     * @return A {@link HyriPartyRank}
     */
    public HyriPartyRank getOldRank() {
        return this.oldRank;
    }

    /**
     * Get the new rank of the party member
     *
     * @return A {@link HyriPartyRank}
     */
    public HyriPartyRank getNewRank() {
        return this.newRank;
    }

}
