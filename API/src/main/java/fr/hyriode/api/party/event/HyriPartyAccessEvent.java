package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:49
 */
public class HyriPartyAccessEvent extends HyriPartyEvent {

    /** The boolean that has been changed in the party access */
    private final boolean privateParty;

    /**
     * Constructor of {@link HyriPartyEvent}
     *
     * @param party The {@link IHyriParty} object
     */
    public HyriPartyAccessEvent(IHyriParty party) {
        super(party);
        this.privateParty = party.isPrivate();
    }

    /**
     * Check if the access of the party is now private
     *
     * @return <code>true</code> if yes
     */
    public boolean isPrivateParty() {
        return this.privateParty;
    }

}
