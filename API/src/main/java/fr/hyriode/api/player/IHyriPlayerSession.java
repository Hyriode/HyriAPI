package fr.hyriode.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.util.DataDictionary;
import fr.hyriode.api.util.Skin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 18:33.<br>
 *
 * A session used to store useful information while the player is connected on the network.
 */
public interface IHyriPlayerSession {

    /**
     * Get the unique id of the player
     *
     * @return A player {@link UUID}
     */
    UUID getPlayerId();

    /**
     * Get the formatted name of the player. It takes {@link IHyriNickname} in account.
     *
     * @return A formatted name
     */
    String getNameWithRank();

    /**
     * Get the time when the player logged in
     *
     * @return A {@link Date}
     */
    Date getLoginDate();

    /**
     * Get the latest player the player sent a message to
     *
     * @return A player {@link UUID}
     */
    @Nullable UUID getPrivateMessageTarget();

    /**
     * Set the latest player the player sent a message to
     *
     * @param playerId A player {@link UUID}
     */
    void setPrivateMessageTarget(@NotNull UUID playerId);

    /**
     * Get the current party of the player
     *
     * @return A party {@link UUID}
     */
    @Nullable UUID getParty();

    /**
     * Set the current party of the player
     *
     * @param partyId A party {@link UUID}
     */
    void setParty(UUID partyId);

    /**
     * Check whether the player is in a party or not.
     *
     * @return <code>true</code> if yes
     */
    default boolean hasParty() {
        return this.getParty() != null;
    }

    /**
     * Get the current queue of the player
     *
     * @return A queue {@link UUID}
     */
    @Nullable UUID getQueue();

    /**
     * Set the current queue of the player
     *
     * @param queueId A queue {@link UUID}
     */
    void setQueue(UUID queueId);

    /**
     * Check whether the player is in a queue or not.
     *
     * @return <code>true</code> if yes
     */
    default boolean isQueuing() {
        return this.getQueue() != null;
    }

    /**
     * Get the proxy the player is connected through
     *
     * @return A proxy name. E.g. proxy-csq45z
     */
    String getProxy();

    /**
     * Set the proxy the player is connected through
     *
     * @param proxy A proxy name. E.g. proxy-csq45z
     */
    void setProxy(String proxy);

    /**
     * Get the server the player is connected to
     *
     * @return A server name
     */
    String getServer();

    /**
     * Set the server the player is connected to
     *
     * @param server A server name
     */
    void setServer(String server);

    /**
     * Get the last server the player was connected to
     *
     * @return A server name
     */
    String getLastServer();

    /**
     * Set the last server the player was connected to
     *
     * @param lastServer A server name
     */
    void setLastServer(String lastServer);

    /**
     * Get the current nickname of the player.
     *
     * @return A {@link IHyriNickname}
     */
    @Nullable IHyriNickname getNickname();

    /**
     * Create a nickname for the player
     *
     * @param name The name to use as a nickname
     * @param skinOwner The owner of the skin that will be used
     * @param skin The skin that will be used
     * @return The created {@link IHyriNickname}
     */
    @NotNull IHyriNickname createNickname(String name, String skinOwner, Skin skin);

    /**
     * Set the current nickname of the player.
     *
     * @param nickname The new {@link IHyriNickname}
     */
    void setNickname(IHyriNickname nickname);

    /**
     * Check if the player has a nickname
     *
     * @return <code>true</code> if yes
     */
    default boolean hasNickname() {
        return this.getNickname() != null;
    }

    /**
     * Check whether the player is playing a game or not.
     *
     * @return <code>true</code> if yes
     */
    boolean isPlaying();

    /**
     * Set whether the player is playing a game or not.
     *
     * @param playing <code>true</code> if yes
     */
    void setPlaying(boolean playing);

    /**
     * Check whether the player is moderating or not.
     *
     * @return <code>true</code> if yes
     */
    boolean isModerating();

    /**
     * Set whether the player is moderating or not.
     *
     * @param moderating <code>true</code> if yes
     */
    void setModerating(boolean moderating);

    /**
     * Check whether the player is vanished or not.
     *
     * @return <code>true</code> if yes
     */
    boolean isVanished();

    /**
     * Set whether the player is vanished or not.
     *
     * @param vanished <code>true</code> if yes
     */
    void setVanished(boolean vanished);

    /**
     * Get the data dictionary linked to the session.
     *
     * @return A {@link DataDictionary} object
     */
    DataDictionary getData();

    /**
     * Update the session in cache.
     */
    default void update() {
        HyriAPI.get().getPlayerManager().updateSession(this);
    }

    /**
     * Get the session of a given player
     *
     * @param playerId The {@link UUID} of the player
     * @return The found {@link IHyriPlayerSession}
     */
    static @Nullable IHyriPlayerSession get(UUID playerId) {
        return HyriAPI.get().getPlayerManager().getSession(playerId);
    }

}
