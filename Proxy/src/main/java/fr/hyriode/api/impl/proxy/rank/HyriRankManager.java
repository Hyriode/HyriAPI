package fr.hyriode.api.impl.proxy.rank;

import fr.hyriode.api.impl.common.rank.HyriCommonRankManager;
import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriRank;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:49
 */
public class HyriRankManager extends HyriCommonRankManager {

    @Override
    public HyriRank getRank(EHyriRank rank) {
        if (rank != null) {
            return EHyriRankImpl.getByName(rank.getName());
        }
        return null;
    }

}
