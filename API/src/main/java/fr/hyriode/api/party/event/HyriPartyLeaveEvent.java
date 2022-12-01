package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.IHyriParty.RemoveReason;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:54
 */
public class HyriPartyLeaveEvent extends HyriPartyMemberEvent {

    /** The reason why the player left the party */
    private final IHyriParty.RemoveReason reason;

    /**
     * Constructor of {@link HyriPartyLeaveEvent}.<br>
     * This event is triggered when a member of a party left the network or the party.
     *
     * @param party The {@link IHyriParty} object
     * @param member The {@link UUID} of the member
     * @param reason The reason why the player left the party
     */
    public HyriPartyLeaveEvent(IHyriParty party, UUID member, IHyriParty.RemoveReason reason) {
        super(party, member);
        this.reason = reason;
    }

    /**
     * Check if the player that left the network or the party is the leader
     *
     * @return <code>true</code> if the player is the party leader
     */
    public boolean isLeader() {
        return this.getParty().getLeader() == this.member;
    }

    /**
     * Get the reason why the player left the party
     *
     * @return A {@link RemoveReason}
     */
    public IHyriParty.RemoveReason getReason() {
        return this.reason;
    }

}
