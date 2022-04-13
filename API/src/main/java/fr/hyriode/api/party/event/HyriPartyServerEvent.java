package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 09:10
 */
public class HyriPartyServerEvent extends HyriPartyEvent {

    /** The old server where the party was */
    private final String oldServer;
    /** The new server where the party is */
    private final String newServer;

    /**
     * Constructor of {@link HyriPartyEvent}
     *
     * @param party The {@link IHyriParty} object
     * @param oldServer A server name
     * @param newServer A server name
     */
    public HyriPartyServerEvent(IHyriParty party, String oldServer, String newServer) {
        super(party);
        this.oldServer = oldServer;
        this.newServer = newServer;
    }

    /**
     * Get the name of the old server where the party was
     *
     * @return A server name
     */
    public String getOldServer() {
        return this.oldServer;
    }

    /**
     * Get the name of the new server where the party is
     *
     * @return A server name
     */
    public String getNewServer() {
        return this.newServer;
    }

}
