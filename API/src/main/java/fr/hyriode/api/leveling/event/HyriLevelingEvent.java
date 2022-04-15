package fr.hyriode.api.leveling.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 15:48
 */
public abstract class HyriLevelingEvent extends HyriEvent {

    /** The player that owns the leveling */
    private final IHyriPlayer player;
    /** The leveling object */
    private final IHyriLeveling leveling;

    /**
     * Constructor of {@link HyriLevelingEvent}
     *
     * @param player The player
     * @param leveling The leveling
     */
    public HyriLevelingEvent(IHyriPlayer player, IHyriLeveling leveling) {
        this.player = player;
        this.leveling = leveling;
    }

    /**
     * Get the player that owns the leveling
     *
     * @return A {@link IHyriPlayer}
     */
    public IHyriPlayer getPlayer() {
        return this.player;
    }

    /**
     * Get the leveling that triggered the event
     *
     * @return The {@link IHyriPlayer} object
     */
    public IHyriLeveling getLeveling() {
        return this.leveling;
    }

}
