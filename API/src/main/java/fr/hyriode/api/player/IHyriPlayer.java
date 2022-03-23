package fr.hyriode.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.settings.IHyriPlayerSettings;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 18:40
 */
public interface IHyriPlayer {

    /**
     * Check if the player is currently connected on the network
     *
     * @return <code>true</code> if he is connected
     */
    boolean isOnline();

    /**
     * Set if the player is connected or not
     *
     * @param online New online state
     */
    void setOnline(boolean online);

    /**
     * Get default player name
     *
     * @return Default name
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
     * @return Custom name
     */
    String getCustomName();

    /**
     * Set player's custom name
     *
     * @param customName Player's custom name
     */
    void setCustomName(String customName);

    /**
     * Get current display name: name or custom name
     *
     * @return Display name
     */
    String getDisplayName();

    /**
     * Get if player has a custom name
     *
     * @return <code>true</code> if player has a custom name
     */
    default boolean hasCustomName() {
        return this.getCustomName() != null;
    }

    /**
     * Get player uuid
     *
     * @return Player {@link UUID}
     */
    UUID getUniqueId();

    /**
     * Get the first login {@link Date} of the player
     *
     * @return {@link Date}
     */
    Date getFirstLoginDate();

    /**
     * Get the last login {@link Date} of the player
     *
     * @return {@link Date}
     */
    Date getLastLoginDate();

    /**
     * Set the last login {@link Date} of the player
     *
     * @param date The new {@link Date}
     */
    void setLastLoginDate(Date date);

    /**
     * Get player play time on the network
     *
     * @return Player play time
     */
    long getPlayTime();

    /**
     * Set player play time on the network
     *
     * @param time New player play time
     */
    void setPlayTime(long time);

    /**
     * Get player rank
     *
     * @return {@link HyriRank}
     */
    HyriRank getRank();

    /**
     * Set player rank
     *
     * @param rank {@link HyriRank}
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
     * @return Hyris money
     */
    IHyriMoney getHyris();

    /**
     * Get player Hyode money
     *
     * @return Hyode money
     */
    IHyriMoney getHyode();

    /**
     * Get the party {@link UUID} of the player
     *
     * @return Player party
     */
    UUID getParty();

    /**
     * Set the party {@link UUID} of the player
     *
     * @param party Party {@link UUID}
     */
    void setParty(UUID party);

    /**
     * Check if the player is in a member of a party
     *
     * @return <code>true</code> if player has one
     */
    boolean hasParty();

    /**
     * Get the settings of the player
     *
     * @return Player settings
     */
    IHyriPlayerSettings getSettings();

    /**
     * Set the settings of the player
     *
     * @param settings New settings
     */
    void setSettings(IHyriPlayerSettings settings);

    /**
     * Get the name of the current server where the player is.<br>
     * Warning: if the player is not connected, it will return <code>null</code>
     *
     * @return A server name
     */
    String getCurrentServer();

    /**
     * Set the current server where the player is connected
     *
     * @param currentServer The name of the server
     */
    void setCurrentServer(String currentServer);

    /**
     * Get the last server where the player was.<br>
     * Warning: if the player is connected on a server it will return the old one not the current. To get the current server use {@link #getCurrentServer()}
     *
     * @return A server name
     */
    String getLastServer();

    /**
     * Set the last server where the player was connected
     *
     * @param lastServer The name of the server
     */
    void setLastServer(String lastServer);

    /**
     * Get the name of the proxy that manages the player
     *
     * @return A proxy name
     */
    String getCurrentProxy();

    /**
     * Set the current proxy that manages the player
     *
     * @param currentProxy A proxy name
     */
    void setCurrentProxy(String currentProxy);

    /**
     * Get the handler of player's friends
     *
     * @return A {@link IHyriFriendHandler} instance
     */
    IHyriFriendHandler getFriendHandler();

    /**
     * Update the player account in database
     */
    default void update() {
        HyriAPI.get().getPlayerManager().sendPlayer(this);
    }

}