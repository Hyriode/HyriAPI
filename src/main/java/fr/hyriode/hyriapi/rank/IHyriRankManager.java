package fr.hyriode.hyriapi.rank;

import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 11:26
 */
public interface IHyriRankManager {

    /**
     * Get rank instance
     *
     * @param rank - Rank type
     * @return - {@link HyriRank}
     */
    HyriRank getRank(EHyriRank rank);

    /**
     * Get rank instance
     *
     * @param name - Rank name
     * @return - {@link HyriRank}
     */
    HyriRank getRank(String name);

    /**
     * Add a permission to a given rank
     *
     * @param rank - Given rank
     * @param permission - Given permission (to add)
     */
    void addPermission(HyriRank rank, HyriPermission permission);

    /**
     * Remove a permission from a given rank
     *
     * @param rank - Given rank
     * @param permission - Given permission (to remove)
     */
    void removePermission(HyriRank rank, HyriPermission permission);

    /**
     * Get all ranks
     *
     * @return - A list of rank
     */
    List<HyriRank> getRanks();

}
