package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:46
 */
public class HyriPartyCreatedEvent extends HyriPartyEvent {

    /**
     * Constructor of {@link HyriPartyCreatedEvent}
     *
     * @param party The {@link IHyriParty} object
     */
    public HyriPartyCreatedEvent(IHyriParty party) {
        super(party);
    }

}
