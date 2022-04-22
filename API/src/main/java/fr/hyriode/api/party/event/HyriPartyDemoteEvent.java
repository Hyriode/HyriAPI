package fr.hyriode.api.party.event;

import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:52
 */
public class HyriPartyDemoteEvent extends HyriPartyRankEvent {

    /**
     * Constructor of {@link HyriPartyDemoteEvent}
     *
     * @param party   The {@link IHyriParty} object
     * @param member  The {@link UUID} of the member
     * @param oldRank A {@link HyriPartyRank}
     * @param newRank A {@link HyriPartyRank}
     */
    public HyriPartyDemoteEvent(IHyriParty party, UUID member, HyriPartyRank oldRank, HyriPartyRank newRank) {
        super(party, member, oldRank, newRank);
    }

}
