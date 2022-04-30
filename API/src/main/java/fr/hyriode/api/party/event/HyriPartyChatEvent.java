package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 29/04/2022 at 22:23
 */
public class HyriPartyChatEvent extends HyriPartyEvent {

    private final Action action;

    /**
     * Constructor of {@link HyriPartyEvent}
     *
     * @param party The {@link IHyriParty} object
     * @param action The action done on the chat
     */
    public HyriPartyChatEvent(IHyriParty party, Action action) {
        super(party);
        this.action = action;
    }

    public Action getAction() {
        return this.action;
    }

    public enum Action {
        ENABLED,
        DISABLED;

        public static Action from(boolean action) {
            return action ? ENABLED : DISABLED;
        }

    }

}
