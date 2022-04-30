package fr.hyriode.api.party.event;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:46
 */
public abstract class HyriPartyEvent extends HyriEvent {

    /** The party that triggered the event */
    protected final UUID party;

    /**
     * Constructor of {@link HyriPartyEvent}
     *
     * @param party The {@link IHyriParty} object
     */
    public HyriPartyEvent(IHyriParty party) {
        this.party = party.getId();
    }

    /**
     * Get the party that triggered the event
     *
     * @return The {@link IHyriParty} object
     */
    public IHyriParty getParty() {
        return HyriAPI.get().getPartyManager().getParty(this.party);
    }

}
