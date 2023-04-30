package fr.hyriode.api.event.model.money;

import fr.hyriode.api.event.HyriEvent;

public class HyriCreditMoneyEvent extends HyriEvent {

    private final long hyris;
    private final long hyodes;

    public HyriCreditMoneyEvent(long hyris, long hyodes) {
        this.hyris = hyris;
        this.hyodes = hyodes;
    }

    public long getHyris() {
        return this.hyris;
    }

    public long getHyodes() {
        return this.hyodes;
    }
}
