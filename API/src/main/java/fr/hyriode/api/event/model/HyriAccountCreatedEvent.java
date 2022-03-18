package fr.hyriode.api.event.model;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 08/03/2022 at 21:06
 */
public class HyriAccountCreatedEvent extends HyriEvent {

    /** The created {@link IHyriPlayer} account */
    private final IHyriPlayer player;

    /**
     * Constructor of {@link HyriAccountCreatedEvent}
     *
     * @param player The {@link IHyriPlayer} account
     */
    public HyriAccountCreatedEvent(IHyriPlayer player) {
        this.player = player;
    }

    /**
     * Get the {@link IHyriPlayer} account that has been created
     *
     * @return A {@link IHyriPlayer}
     */
    public IHyriPlayer getPlayer() {
        return this.player;
    }

}
