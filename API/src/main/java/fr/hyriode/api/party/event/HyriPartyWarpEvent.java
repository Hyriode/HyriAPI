package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 29/04/2022 at 23:40
 */
public class HyriPartyWarpEvent extends HyriPartyEvent {

    private final String server;

    /**
     * Constructor of {@link HyriPartyEvent}
     *
     * @param party The {@link IHyriParty} object
     * @param server The server where the party has been warped
     */
    public HyriPartyWarpEvent(IHyriParty party, String server) {
        super(party);
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }

}
