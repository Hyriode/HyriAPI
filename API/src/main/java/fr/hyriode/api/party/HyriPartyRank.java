package fr.hyriode.api.party;

public enum HyriPartyRank {

    LEADER(2, "leader", true, true, true, true, true),
    OFFICER(1, "officer", true, false, false, true, true),
    MEMBER(0, "member", false, false, false, false, false);

    public static final HyriPartyRank[] VALUES = HyriPartyRank.values();

    private final int id;
    private final String displayKey;
    private final boolean sendInvitations;
    private final boolean editRank;
    private final boolean disband;
    private final boolean kick;
    private final boolean mute;

    HyriPartyRank(int id, String displayKey, boolean sendInvitations, boolean editRank, boolean disband, boolean kick, boolean mute) {
        this.id = id;
        this.displayKey = displayKey;
        this.sendInvitations = sendInvitations;
        this.editRank = editRank;
        this.disband = disband;
        this.kick = kick;
        this.mute = mute;
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

    public String getDisplayKey() {
        return this.displayKey;
    }

    public boolean canSendInvitations() {
        return this.sendInvitations;
    }

    public boolean canEditRank() {
        return this.editRank;
    }

    public boolean canDisband() {
        return this.disband;
    }

    public boolean canKick() {
        return this.kick;
    }

    public boolean canMute() {
        return this.mute;
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
