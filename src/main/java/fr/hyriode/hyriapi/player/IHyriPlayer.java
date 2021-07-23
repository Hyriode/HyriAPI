package fr.hyriode.hyriapi.player;

import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.party.IHyriParty;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 18:40
 */
public interface IHyriPlayer {

    /**
     * Get default player name
     *
     * @return - Default name
     */
    String getName();

    /**
     * Get the custom name of player
     *
     * @return - Custom name
     */
    String getCustomName();

    /**
     * Get current display name: name or custom name
     *
     * @return - Display name
     */
    String getDisplayName();

    /**
     * Get if player has nickname
     *
     * @return <code>true</code> if player has nickname
     */
    boolean hasNickname();

    /**
     * Get player uuid
     *
     * @return - Player {@link UUID}
     */
    UUID getUUID();

    /**
     * Get player Hyris money
     *
     * @return - Hyris money
     */
    IHyriMoney getHyris();

    /**
     * Get player Hyode money
     *
     * @return - Hyode money
     */
    IHyriMoney getHyode();

    /**
     * Replace Hyris or Hyode if instance of
     *
     * @param money - New money
     */
    void setMoney(IHyriMoney money);

    /**
     * Get the party {@link UUID} of the player
     *
     * @return - Player party
     */
    UUID getParty();

    /**
     * Set the party {@link UUID} of the player
     *
     * @param party - Party {@link UUID}
     */
    void setParty(UUID party);

    /**
     * Get if the player is in a party
     *
     * @return - <code>true</code> if player has one
     */
    boolean hasParty();

    /**
     * Get the settings of the player
     *
     * @return - Player settings
     */
    IHyriPlayerSettings getSettings();

    /**
     * Set the settings of the player
     *
     * @param settings - New settings
     */
    void setSettings(IHyriPlayerSettings settings);

}