package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:54
 */
public class HyriPartyLeaveEvent extends HyriPartyMemberEvent {

    /**
     * Constructor of {@link HyriPartyLeaveEvent}.<br>
     * This event is triggered when a member of a party left the network or the party.
     *
     * @param party  The {@link IHyriParty} object
     * @param member The {@link UUID} of the member
     */
    public HyriPartyLeaveEvent(IHyriParty party, UUID member) {
        super(party, member);
    }

    /**
     * Check if the player that left the network or the party is the leader
     *
     * @return <code>true</code> if the player is the party leader
     */
    public boolean isLeader() {
        return this.party.getLeader() == this.member;
    }

}
