package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 29/04/2022 at 21:23
 */
public class HyriPartyKickEvent extends HyriPartyMemberEvent {

    private final UUID kicker;

    /**
     * Constructor of {@link HyriPartyLeaveEvent}.<br>
     *
     *  @param party  The {@link IHyriParty} object
     * @param member The {@link UUID} of the member
     * @param kicker The player that kicked the member from the party
     */
    public HyriPartyKickEvent(IHyriParty party, UUID member, UUID kicker) {
        super(party, member);
        this.kicker = kicker;
    }

    public UUID getKicker() {
        return this.kicker;
    }

}
