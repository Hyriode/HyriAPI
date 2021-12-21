package fr.hyriode.hyriapi.player;

import fr.hyriode.hyriapi.cosmetic.HyriCosmetic;
import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.rank.EHyriRank;
import fr.hyriode.hyriapi.rank.HyriRank;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;

import java.util.Date;
import java.util.List;
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
     * Set player's name<br>
     * Warning: Use this method ONLY to change the real player name
     *
     * @param name Player's name
     */
    void setName(String name);

    /**
     * Get the custom name of player
     *
     * @return - Custom name
     */
    String getCustomName();

    /**
     * Set player's custom name
     *
     * @param customName - Player's custom name
     */
    void setCustomName(String customName);

    /**
     * Get current display name: name or custom name
     *
     * @return - Display name
     */
    String getDisplayName();

    /**
     * Get if player has a custom name
     *
     * @return <code>true</code> if player has a custom name
     */
    boolean hasCustomName();

    /**
     * Get player uuid
     *
     * @return - Player {@link UUID}
     */
    UUID getUUID();

    /**
     * Get the first login {@link Date} of the player
     *
     * @return - {@link Date}
     */
    Date getFirstLoginDate();

    /**
     * Get the last login {@link Date} of the player
     *
     * @return - {@link Date}
     */
    Date getLastLoginDate();

    /**
     * Set the last login {@link Date} of the player
     *
     * @param date - The new {@link Date}
     */
    void setLastLoginDate(Date date);

    /**
     * Get player play time on the network
     *
     * @return - Player play time
     */
    long getPlayTime();

    /**
     * Set player play time on the network
     *
     * @param time - New player play time
     */
    void setPlayTime(long time);

    /**
     * Get player rank
     *
     * @return - {@link HyriRank}
     */
    HyriRank getRank();

    /**
     * Set player rank
     *
     * @param rank - {@link HyriRank}
     */
    void setRank(HyriRank rank);

    /**
     * Set player rank
     *
     * @param rank Rank type
     */
    default void setRank(EHyriRank rank) {
        this.setRank(rank.get());
    }

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
     * Get money by name
     *
     * @param name - Money name
     * @return - {@link IHyriMoney}
     */
    IHyriMoney getMoney(String name);

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
     * Check if the player is in a member of a party
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

    /**
     * Get the cosmetics of the player
     *
     * @return A list of cosmetics
     */
    List<Class<? extends HyriCosmetic>> getCosmetics();

    /**
     * Add a cosmetic to the player
     *
     * @param cosmetic The cosmetic to add
     */
    void addCosmetic(Class<? extends HyriCosmetic> cosmetic);

    /**
     * Remove cosmetic to the player
     *
     * @param cosmetic The cosmetic to remove
     */
    void removeCosmetic(Class<? extends HyriCosmetic> cosmetic);

}