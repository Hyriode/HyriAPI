package fr.hyriode.api.impl.common.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.cosmetic.HyriCosmetic;
import fr.hyriode.api.impl.common.money.Hyode;
import fr.hyriode.api.impl.common.money.Hyris;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettings;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.settings.IHyriPlayerSettings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayer implements IHyriPlayer {

    private String name;
    private String customName = null;
    private final UUID uuid;

    private Date lastLoginDate;
    private final Date firstLoginDate;

    private long playTime;

    private String rank;

    private Hyode hyode;
    private Hyris hyris;

    private UUID party;

    private HyriPlayerSettings settings;

    private final List<Class<? extends HyriCosmetic>> cosmetics;

    public HyriPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.firstLoginDate = new Date(System.currentTimeMillis());
        this.lastLoginDate = this.firstLoginDate;
        this.rank = EHyriRank.PLAYER.getName();
        this.hyris = new Hyris(this.uuid);
        this.hyode = new Hyode(this.uuid);
        this.party = null;
        this.settings = (HyriPlayerSettings) HyriAPI.get().getPlayerSettingsManager().createPlayerSettings();
        this.cosmetics = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCustomName() {
        return this.customName;
    }

    @Override
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public String getDisplayName() {
        return this.hasCustomName() ? this.getCustomName() : this.getName();
    }

    @Override
    public boolean hasCustomName() {
        return this.getCustomName() != null;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public Date getFirstLoginDate() {
        return this.firstLoginDate;
    }

    @Override
    public Date getLastLoginDate() {
        return this.lastLoginDate;
    }

    @Override
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public long getPlayTime() {
        return this.playTime;
    }

    @Override
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    @Override
    public HyriRank getRank() {
        return HyriAPI.get().getRankManager().getRank(this.rank);
    }

    @Override
    public void setRank(HyriRank rank) {
        this.rank = rank.getName();
    }

    @Override
    public IHyriMoney getHyris() {
        return this.hyris;
    }

    @Override
    public IHyriMoney getHyode() {
        return this.hyode;
    }

    @Override
    public UUID getParty() {
        return this.party;
    }

    @Override
    public void setParty(UUID party) {
        this.party = party;
    }

    @Override
    public boolean hasParty() {
        return this.getParty() != null;
    }

    @Override
    public IHyriPlayerSettings getSettings() {
        return this.settings;
    }

    @Override
    public void setSettings(IHyriPlayerSettings settings) {
        this.settings = (HyriPlayerSettings) settings;
    }

    @Override
    public List<Class<? extends HyriCosmetic>> getCosmetics() {
        return this.cosmetics;
    }

    @Override
    public void addCosmetic(Class<? extends HyriCosmetic> cosmetic) {
        this.cosmetics.add(cosmetic);
    }

    @Override
    public void removeCosmetic(Class<? extends HyriCosmetic> cosmetic) {
        this.cosmetics.remove(cosmetic);
    }

}
