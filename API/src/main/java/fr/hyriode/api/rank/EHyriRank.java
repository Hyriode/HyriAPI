package fr.hyriode.api.rank;

import fr.hyriode.api.HyriAPI;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 11:15
 */
public enum EHyriRank {

    /** Player */
    PLAYER(12, "player"),
    VIP(11, "vip"),
    VIP_PLUS(10, "vip+"),
    EPIC(9, "epic"),

    /** Content creator */
    PARTNER(8, "partner"),

    /** Staff */
    STAFF(7, "staff"),
    HELPER(6, "helper"),
    DESIGNER(5, "designer"),
    BUILDER(4, "builder"),
    MODERATOR(3, "moderator"),
    DEVELOPER(2, "developer"),
    MANAGER(1, "manager"),
    ADMINISTRATOR(0, "administrator");

    private final int id;
    private final String name;

    EHyriRank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public HyriRank get() {
        return HyriAPI.get().getRankManager().getRank(this);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static EHyriRank getByName(String name) {
        for (EHyriRank rank : values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

}
