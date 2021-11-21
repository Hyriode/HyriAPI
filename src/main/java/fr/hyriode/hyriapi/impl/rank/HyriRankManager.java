package fr.hyriode.hyriapi.impl.rank;

import fr.hyriode.hyriapi.rank.EHyriRank;
import fr.hyriode.hyriapi.rank.HyriPermission;
import fr.hyriode.hyriapi.rank.HyriRank;
import fr.hyriode.hyriapi.rank.IHyriRankManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
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
    public void addPermission(EHyriRank rank, HyriPermission permission) {
        this.addPermission(this.getRank(rank), permission);
    }

    @Override
    public void removePermission(HyriRank rank, HyriPermission permission) {
        rank.getPermissions().remove(permission);
    }

    @Override
    public void removePermission(EHyriRank rank, HyriPermission permission) {
        this.removePermission(this.getRank(rank), permission);
    }

    @Override
    public boolean hasPermission(EHyriRank rank, HyriPermission permission) {
        return this.getRank(rank).hasPermission(permission);
    }

    @Override
    public List<HyriRank> getRanks() {
        final List<HyriRank> ranks = new ArrayList<>();

        for (EHyriRank rank : EHyriRank.values()) {
            ranks.add(this.getRank(rank));
        }

        return ranks;
    }

}
