package fr.hyriode.api.party;

import fr.hyriode.api.language.HyriLanguage;

public enum HyriPartyRank {

    LEADER(2, "Chef", "Leader", true, true, true, true, true, true),
    OFFICER(1, "Officier", "Officer", true, false, false, false, true, true),
    MEMBER(0, "Membre", "Member", false, false, false, false, false, false);

    public static final HyriPartyRank[] VALUES = HyriPartyRank.values();

    private final int id;
    private final String french;
    private final String english;
    private final boolean sendInvitations;
    private final boolean editRank;
    private final boolean disband;
    private final boolean warp;
    private final boolean kick;
    private final boolean mute;

    HyriPartyRank(int id, String french, String english, boolean sendInvitations, boolean editRank, boolean disband, boolean warp, boolean kick, boolean mute) {
        this.id = id;
        this.french = french;
        this.english = english;
        this.sendInvitations = sendInvitations;
        this.editRank = editRank;
        this.disband = disband;
        this.warp = warp;
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

    public String getFrench() {
        return this.french;
    }

    public String getEnglish() {
        return this.english;
    }

    public String getDisplay(HyriLanguage language) {
        return language == HyriLanguage.FR ? this.french : this.english;
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

    public boolean canWarp() {
        return this.warp;
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
