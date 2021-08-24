package fr.hyriode.hyriapi.impl.api.rank;

import fr.hyriode.hyriapi.rank.HyriRank;
import org.bukkit.ChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 18:41
 */
public enum EHyriRankImpl {

    /** Player */
    PLAYER(new DefaultHyriRank("player", ChatColor.GRAY + "Joueur", "")),
    CUSTOM(new DefaultHyriRank("custom", ChatColor.DARK_GRAY + "Custom", "")),

    /** Content creator */
    STREAMER(new DefaultHyriRank("streamer", ChatColor.DARK_PURPLE + "Streamer", "")),
    YOUTUBER(new DefaultHyriRank("youtuber", ChatColor.GOLD + "Youtubeur", "")),

    /** Staff */
    BUILDER(new DefaultHyriRank("builder", ChatColor.DARK_GREEN + "Buildeur", "")),
    DESIGNER(new DefaultHyriRank("designer", ChatColor.DARK_GREEN + "Graphiste", "")),
    DEVELOPER(new DefaultHyriRank("developer", ChatColor.DARK_GREEN + "Développeur", "")),
    HELPER(new DefaultHyriRank("helper", ChatColor.BLUE + "Assistant", "")),
    MODERATOR(new DefaultHyriRank("moderator", ChatColor.BLUE + "Modérateur", "")),
    SUPER_MODERATOR(new DefaultHyriRank("super_moderator", ChatColor.BLUE + "SuperModérateur", "")),
    MANAGER(new DefaultHyriRank("manager", ChatColor.RED + "Responsable", "")),
    ADMINISTRATOR(new DefaultHyriRank("administrator", ChatColor.DARK_RED + "Administrateur", ""));

    private final HyriRank rank;

    EHyriRankImpl(HyriRank rank) {
        this.rank = rank;
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
