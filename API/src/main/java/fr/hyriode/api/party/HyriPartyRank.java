package fr.hyriode.api.party;

public enum HyriPartyRank {

    LEADER(2, "party.rank.leader"),
    OFFICER(1, "party.rank.officer"),
    MEMBER(0, "party.rank.member");

    public static final HyriPartyRank[] VALUES = HyriPartyRank.values();

    private final int id;
    private final String language;


    HyriPartyRank(int id, String language) {
        this.id = id;
        this.language = language;
    }

    public boolean isSuperior(HyriPartyRank other) {
        return this.id >= other.getId();
    }

    public boolean isInferior(HyriPartyRank other) {
        return this.id <= other.getId();
    }

    public boolean isSame(HyriPartyRank other) {
        return this.id == other.getId();
    }

    public HyriPartyRank getSuperior() {
        return HyriPartyRank.getById(this.id + 1);
    }

    public HyriPartyRank getInferior() {
        return HyriPartyRank.getById(this.id - 1);
    }

    public int getId() {
        return this.id;
    }

    public String getLanguage() {
        return this.language;
    }

    public static HyriPartyRank getById(int id) {
        for (HyriPartyRank rank : VALUES) {
            if (rank.getId() == id) {
                return rank;
            }
        }
        return null;
    }

}
