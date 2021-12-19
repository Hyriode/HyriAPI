package fr.hyriode.hyriapi.rank;

import fr.hyriode.hyriapi.HyriAPI;

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

}
