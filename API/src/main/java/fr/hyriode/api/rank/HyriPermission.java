package fr.hyriode.api.rank;

import fr.hyriode.api.HyriAPI;

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
     * @param ranks Ranks which will have the permission
     */
    default void add(EHyriRank... ranks) {
        for (EHyriRank rank : ranks) {
            HyriAPI.get().getRankManager().addPermission(rank, this);
        }
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
