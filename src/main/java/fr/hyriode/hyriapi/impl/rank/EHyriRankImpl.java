package fr.hyriode.hyriapi.impl.rank;

import fr.hyriode.hyriapi.rank.HyriRank;
import org.bukkit.ChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public enum EHyriRankImpl {

    /** Player */
    PLAYER(11, new DefaultHyriRank("player", ChatColor.GRAY + "Joueur", "")),
    CUSTOM(10, new DefaultHyriRank("custom", ChatColor.DARK_GRAY + "Custom", "")),

    /** Content creator */
    STREAMER(9, new DefaultHyriRank("streamer", ChatColor.DARK_PURPLE + "Streamer", "")),
    YOUTUBER(8, new DefaultHyriRank("youtuber", ChatColor.GOLD + "Youtubeur", "")),

    /** Staff */
    BUILDER(7, new DefaultHyriRank("builder", ChatColor.DARK_GREEN + "Buildeur", "")),
    DESIGNER(6, new DefaultHyriRank("designer", ChatColor.DARK_GREEN + "Graphiste", "")),
    DEVELOPER(5,new DefaultHyriRank("developer", ChatColor.DARK_GREEN + "Développeur", "")),
    HELPER(4, new DefaultHyriRank("helper", ChatColor.BLUE + "Assistant", "")),
    MODERATOR(3, new DefaultHyriRank("moderator", ChatColor.BLUE + "Modérateur", "")),
    SUPER_MODERATOR(2, new DefaultHyriRank("super_moderator", ChatColor.BLUE + "SuperMod.", "")),
    MANAGER(1, new DefaultHyriRank("manager", ChatColor.RED + "Responsable", "")),
    ADMINISTRATOR(0, new DefaultHyriRank("administrator", ChatColor.DARK_RED + "Admin", ""));

    private final int id;
    private final HyriRank rank;

    EHyriRankImpl(int id, HyriRank rank) {
        this.id = id;
        this.rank = rank;
    }

    public int getId() {
        return this.id;
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

    public static EHyriRankImpl getEnumByName(String name) {
        for (EHyriRankImpl rank : values()) {
            if (rank.get().getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    public static EHyriRankImpl getById(int id) {
        for (EHyriRankImpl rank : values()) {
            if (rank.getId() == id) {
                return rank;
            }
        }
        return null;
    }

}
