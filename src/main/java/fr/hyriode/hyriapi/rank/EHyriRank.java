package fr.hyriode.hyriapi.rank;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 11:15
 */
public enum EHyriRank {

    /** Player */
    PLAYER(11, "player"),
    VIP(10, "vip"),
    VIP_PLUS(9, "vip+"),
    EPIC(8, "epic"),

    /** Content creator */
    STREAMER(7, "streamer"),
    YOUTUBER(6, "youtuber"),

    /** Staff */
    STAFF(5, "staff"),
    HELPER(4, "helper"),
    DEVELOPER(3, "developer"),
    MODERATOR(2, "moderator"),
    MANAGER(1, "manager"),
    ADMINISTRATOR(0, "administrator");

    private final int id;
    private final String name;

    EHyriRank(int id, String name) {
        this.id = id;
        this.name = name;
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
