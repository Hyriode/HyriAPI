package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:46
 */
public class HyriPartyDisbandEvent extends HyriPartyEvent {

    /** The reason that triggered the event */
    private final IHyriParty.DisbandReason reason;

    /**
     * Constructor of {@link HyriPartyDisbandEvent}
     *
     * @param party The {@link IHyriParty} object
     * @param reason A {@link IHyriParty.DisbandReason}
     */
    public HyriPartyDisbandEvent(IHyriParty party, IHyriParty.DisbandReason reason) {
        super(party);
        this.reason = reason;
    }

    /**
     * Get the reason that triggered the event
     *
     * @return A {@link IHyriParty.DisbandReason}
     */
    public IHyriParty.DisbandReason getReason() {
        return this.reason;
    }

}
