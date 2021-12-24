package fr.hyriode.hyriapi.rank;

import fr.hyriode.hyriapi.HyriAPI;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 11:08
 */
public interface HyriPermission {

    /**
     * Add the permission to a given rank
     *
     * @param rank Rank which will have the permission
     */
    default void add(EHyriRank rank) {
        HyriAPI.get().getRankManager().addPermission(rank, this);
    }

    /**
     * Check if a player has the permission
     *
     * @param uuid Player's unique id
     * @return <code>true</code> if yes
     */
     default boolean has(UUID uuid) {
        return HyriAPI.get().getPlayerManager().hasPermission(uuid, this);
     }

}
