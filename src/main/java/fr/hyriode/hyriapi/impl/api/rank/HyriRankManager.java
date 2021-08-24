package fr.hyriode.hyriapi.impl.api.rank;

import fr.hyriode.hyriapi.rank.EHyriRank;
import fr.hyriode.hyriapi.rank.HyriPermission;
import fr.hyriode.hyriapi.rank.HyriRank;
import fr.hyriode.hyriapi.rank.IHyriRankManager;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 18:30
 */
public class HyriRankManager implements IHyriRankManager {

    @Override
    public HyriRank getRank(EHyriRank rank) {
        return EHyriRankImpl.valueOf(rank.name()).get();
    }

    @Override
    public HyriRank getRank(String name) {
        return EHyriRankImpl.getByName(name);
    }

    @Override
    public void addPermission(HyriRank rank, HyriPermission permission) {
        rank.getPermissions().add(permission);
    }

    @Override
    public void removePermission(HyriRank rank, HyriPermission permission) {
        rank.getPermissions().remove(permission);
    }

}
