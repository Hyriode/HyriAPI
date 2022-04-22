package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 08:54
 */
public class HyriPartyJoinEvent extends HyriPartyMemberEvent {

    /**
     * Constructor of {@link HyriPartyJoinEvent}.<br>
     * This event is triggered when a new player join a party
     *
     * @param party  The {@link IHyriParty} object
     * @param member The {@link UUID} of the member
     */
    public HyriPartyJoinEvent(IHyriParty party, UUID member) {
        super(party, member);
    }

}
