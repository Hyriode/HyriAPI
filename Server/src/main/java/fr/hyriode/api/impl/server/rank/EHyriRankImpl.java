package fr.hyriode.api.impl.server.rank;

import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.settings.HyriLanguage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public enum EHyriRankImpl {

    /** Player */
    PLAYER(EHyriRank.PLAYER, ChatColor.GRAY, "", ""),
    VIP(EHyriRank.VIP, ChatColor.YELLOW, "VIP", "VIP"),
    VIP_PLUS(EHyriRank.VIP_PLUS,  ChatColor.GREEN, "VIP+", "VIP+"),
    EPIC(EHyriRank.EPIC, ChatColor.AQUA, "Epic", "Epic"),

    /** Content creator */
    STREAMER(EHyriRank.STREAMER, ChatColor.LIGHT_PURPLE, "Streamer", "Streamer"),
    YOUTUBER(EHyriRank.YOUTUBER, ChatColor.GOLD, "Youtubeur", "Youtuber"),

    /** Staff */
    STAFF(EHyriRank.STAFF, ChatColor.DARK_GRAY, "Staff", "Staff"),
    HELPER(EHyriRank.HELPER, ChatColor.DARK_PURPLE, "Assistant", "Helper"),
    DESIGNER(EHyriRank.DESIGNER, ChatColor.GREEN, "Graphiste", "Designer"),
    BUILDER(EHyriRank.BUILDER, ChatColor.GREEN, "Builder", "Builder"),
    MODERATOR(EHyriRank.MODERATOR, ChatColor.DARK_AQUA, "Mod", "Mod"),
    DEVELOPER(EHyriRank.DEVELOPER, ChatColor.DARK_GREEN, "Dev", "Dev"),
    MANAGER(EHyriRank.MANAGER, ChatColor.BLUE, "Resp", "Manager"),
    ADMINISTRATOR(EHyriRank.ADMINISTRATOR, ChatColor.RED, "Admin", "Admin");

    private final EHyriRank initial;
    private final HyriRank rank;

    EHyriRankImpl(EHyriRank initial, ChatColor color, String frenchDisplayName, String englishDisplayName) {
        this.initial = initial;

        final Map<HyriLanguage, String> displayNames = new HashMap<>();

        displayNames.put(HyriLanguage.EN, color + englishDisplayName);
        displayNames.put(HyriLanguage.FR, color + frenchDisplayName);

        this.rank = new HyriRank(this.initial.getName(), displayNames, new ArrayList<>()){};
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
