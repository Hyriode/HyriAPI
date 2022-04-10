package fr.hyriode.api.impl.server.rank;

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
    PLAYER(EHyriRank.PLAYER, net.md_5.bungee.api.ChatColor.GRAY + ""),
    VIP(EHyriRank.VIP, net.md_5.bungee.api.ChatColor.YELLOW + "VIP"),
    VIP_PLUS(EHyriRank.VIP_PLUS,  net.md_5.bungee.api.ChatColor.GREEN + "VIP+"),
    EPIC(EHyriRank.EPIC, net.md_5.bungee.api.ChatColor.AQUA + "Epic"),

    /** Content creator */
    PARTNER(EHyriRank.PARTNER, net.md_5.bungee.api.ChatColor.GOLD + "Partner"),

    /** Staff */
    STAFF(EHyriRank.STAFF, net.md_5.bungee.api.ChatColor.DARK_GRAY + "Staff"),
    HELPER(EHyriRank.HELPER, net.md_5.bungee.api.ChatColor.DARK_PURPLE + "Helper"),
    DESIGNER(EHyriRank.DESIGNER, net.md_5.bungee.api.ChatColor.GREEN + "Designer"),
    BUILDER(EHyriRank.BUILDER, net.md_5.bungee.api.ChatColor.GREEN + "Builder"),
    MODERATOR(EHyriRank.MODERATOR, net.md_5.bungee.api.ChatColor.DARK_AQUA + "Mod"),
    DEVELOPER(EHyriRank.DEVELOPER, net.md_5.bungee.api.ChatColor.DARK_GREEN + "Dev"),
    MANAGER(EHyriRank.MANAGER, net.md_5.bungee.api.ChatColor.BLUE + "Manager"),
    ADMINISTRATOR(EHyriRank.ADMINISTRATOR, ChatColor.RED + "Admin");

    private final EHyriRank initial;
    private final HyriRank rank;

    EHyriRankImpl(EHyriRank initial, String prefix) {
        this.initial = initial;

        this.rank = new HyriRank(this.initial.getName(), prefix, new ArrayList<>()){};
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
