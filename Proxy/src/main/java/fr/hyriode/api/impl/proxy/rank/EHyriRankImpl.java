package fr.hyriode.api.impl.proxy.rank;

import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriRank;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public enum EHyriRankImpl {

    /** Player */
    PLAYER(EHyriRank.PLAYER, ChatColor.GRAY + ""),
    VIP(EHyriRank.VIP, ChatColor.YELLOW + "VIP"),
    VIP_PLUS(EHyriRank.VIP_PLUS,  ChatColor.GREEN + "VIP+"),
    EPIC(EHyriRank.EPIC, ChatColor.AQUA + "Epic"),

    /** Content creator */
    PARTNER(EHyriRank.PARTNER, ChatColor.GOLD + "Partner"),

    /** Staff */
    STAFF(EHyriRank.STAFF, ChatColor.DARK_GRAY + "Staff"),
    HELPER(EHyriRank.HELPER, ChatColor.DARK_PURPLE + "Helper"),
    DESIGNER(EHyriRank.DESIGNER, ChatColor.GREEN + "Designer"),
    BUILDER(EHyriRank.BUILDER, ChatColor.GREEN + "Builder"),
    MODERATOR(EHyriRank.MODERATOR, ChatColor.DARK_AQUA + "Mod"),
    DEVELOPER(EHyriRank.DEVELOPER, ChatColor.DARK_GREEN + "Dev"),
    MANAGER(EHyriRank.MANAGER, ChatColor.BLUE + "Manager"),
    ADMINISTRATOR(EHyriRank.ADMINISTRATOR, ChatColor.RED + "Admin");

    private final EHyriRank initial;
    private final HyriRank rank;

    EHyriRankImpl(EHyriRank initial, String displayName) {
        this.initial = initial;

        this.rank = new HyriRank(this.initial.getName(), displayName, new ArrayList<>()){};
    }

    public EHyriRank getInitial() {
        return this.initial;
    }

    public HyriRank get() {
        return this.rank;
    }

    public static HyriRank getByName(String name) {
        for (EHyriRankImpl rank : values()) {
            if (rank.get().getName().equalsIgnoreCase(name)) {
                return rank.get();
            }
        }
        return null;
    }

}
