package fr.hyriode.api.language;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 14/07/2022 at 22:23
 */
public interface IHyriLanguagePlayer {

    /**
     * Get the unique id of the player
     *
     * @return The player identifier
     */
    UUID getUniqueId();

    /**
     * Get the account of the player
     *
     * @return The {@linkplain IHyriPlayer player account}
     */
    default IHyriPlayer getAccount() {
        return IHyriPlayer.get(this.getUniqueId());
    }

    /**
     * Get the language used by the player
     *
     * @return A {@link HyriLanguage}
     */
    default HyriLanguage getLanguage() {
        return this.getAccount().getSettings().getLanguage();
    }

}
