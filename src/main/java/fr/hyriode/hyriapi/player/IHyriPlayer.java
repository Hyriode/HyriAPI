package fr.hyriode.hyriapi.player;

import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.money.IHyriMoneyCallback;

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
     * Get if player has nickname
     *
     * @return <code>true</code> if player has nickname
     */
    boolean hasNickname();

}