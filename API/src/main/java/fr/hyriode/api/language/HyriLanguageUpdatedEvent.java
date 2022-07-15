package fr.hyriode.api.language;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 14/07/2022 at 22:33
 */
public class HyriLanguageUpdatedEvent extends HyriEvent {

    /** The unique identifier of the concerned player */
    private final UUID playerId;
    /** The account of the player concerned */
    private final IHyriPlayer playerAccount;
    /** The new language */
    private final HyriLanguage language;

    /**
     * Constructor of {@link HyriLanguageUpdatedEvent}
     *
     * @param playerId The player identifier
     * @param language The new language of the player
     */
    public HyriLanguageUpdatedEvent(UUID playerId, HyriLanguage language) {
        this.playerId = playerId;
        this.playerAccount = IHyriPlayer.get(this.playerId);
        this.language = language;
    }

    /**
     * Get the unique id of the player
     *
     * @return The player {@linkplain UUID id}
     */
    public UUID getPlayerId() {
        return this.playerId;
    }

    /**
     * Get the player account
     *
     * @return {@link IHyriPlayer} object
     */
    public IHyriPlayer getPlayerAccount() {
        return this.playerAccount;
    }

    /**
     * Get the new language of the player
     *
     * @return {@link HyriLanguage}
     */
    public HyriLanguage getLanguage() {
        return this.language;
    }

}
