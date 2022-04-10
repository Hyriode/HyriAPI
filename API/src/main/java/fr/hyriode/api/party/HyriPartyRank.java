package fr.hyriode.api.party;

public enum HyriPartyRank {

    LEADER(2, "party.rank.leader"),
    OFFICER(1, "party.rank.officer"),
    MEMBER(0, "party.rank.member");

    private final int id;
    private final String language;

    HyriPartyRank(int id, String language) {
        this.id = id;
        this.language = language;
    }

    public int getId() {
        return this.id;
    }

    public String getLanguage() {
        return this.language;
    }

    public static HyriPartyRank getById(int id) {
        for (HyriPartyRank rank : HyriPartyRank.values()) {
            if (rank.getId() == id) {
                return rank;
            }
        }
        return null;
    }
}
