package fr.hyriode.api.player.nickname;

import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.util.Skin;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 20:49
 */
public interface IHyriNickname {

    /**
     * Get the name used in the nickname
     *
     * @return A name
     */
    String getName();

    /**
     * Set the name used for the nickname
     *
     * @param name The new name
     */
    void setName(String name);

    /**
     * Get the owner of the skin used to nick the player
     *
     * @return A player name
     */
    String getSkinOwner();

    /**
     * Set the owner of the skin used to nick the player
     *
     * @param skinOwner A player name
     */
    void setSkinOwner(String skinOwner);

    /**
     * Get the appearance skin of the nickname
     *
     * @return A {@link Skin} object
     */
    Skin getSkin();

    /**
     * Set the appearance skin of the nickname
     *
     * @param skin New {@link Skin} object
     */
    void setSkin(Skin skin);

    /**
     * Get the appearance rank of the nickname
     *
     * @return A {@link HyriPlayerRankType}
     */
    HyriPlayerRankType getRank();

    /**
     * Set the appearance rank of the nickname
     *
     * @param rank The new {@link HyriPlayerRankType}
     */
    void setRank(HyriPlayerRankType rank);

    /**
     * Update the nickname of the player
     *
     * @param playerId The {@link UUID} of the player
     * @return This {@link IHyriNickname} instance
     */
    IHyriNickname update(UUID playerId);

}
