package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:50
 */
public abstract class HyriPartyMemberEvent extends HyriPartyEvent {

    /** The {@link UUID} of the member concerned by the event */
    protected final UUID member;

    /**
     * Constructor of {@link HyriPartyEvent}
     *
     * @param party The {@link IHyriParty} object
     * @param member The {@link UUID} of the member
     */
    public HyriPartyMemberEvent(IHyriParty party, UUID member) {
        super(party);
        this.member = member;
    }

    /**
     * Get the member that triggered the event
     *
     * @return The player {@link UUID}
     */
    public UUID getMember() {
        return this.member;
    }

}
