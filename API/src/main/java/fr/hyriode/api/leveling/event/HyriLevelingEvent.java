package fr.hyriode.api.leveling.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 15:48
 */
public abstract class HyriLevelingEvent extends HyriEvent {

    /** The player that owns the leveling */
    private final UUID player;
    /** The leveling object */
    private final String leveling;

    /**
     * Constructor of {@link HyriLevelingEvent}
     *
     * @param player The player
     * @param leveling The leveling
     */
    public HyriLevelingEvent(UUID player, String leveling) {
        this.player = player;
        this.leveling = leveling;
    }

    /**
     * Get the player that owns the leveling
     *
     * @return A {@link IHyriPlayer}
     */
    public UUID getPlayer() {
        return this.player;
    }

    public String getLeveling() {
        return this.leveling;
    }

}
