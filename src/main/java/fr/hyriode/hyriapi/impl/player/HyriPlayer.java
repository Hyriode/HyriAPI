package fr.hyriode.hyriapi.impl.player;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.cosmetic.HyriCosmetic;
import fr.hyriode.hyriapi.impl.money.model.HyodeMoney;
import fr.hyriode.hyriapi.impl.money.model.HyrisMoney;
import fr.hyriode.hyriapi.impl.rank.EHyriRankImpl;
import fr.hyriode.hyriapi.impl.settings.HyriPlayerSettings;
import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.rank.HyriRank;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;

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

    private final List<Class<? extends HyriCosmetic>> cosmetics;

    private HyriPlayerSettings settings;

    private UUID party;

    private HyodeMoney hyode;
    private HyrisMoney hyris;

    private String rank;
    private String prefix;

    private long playTime;

    private Date lastLoginDate;
    private final Date firstLoginDate;

    private final UUID uuid;
    private final String name;
    private String customName = null;

    public HyriPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.firstLoginDate = new Date(System.currentTimeMillis());
        this.lastLoginDate = this.firstLoginDate;
        this.rank = EHyriRankImpl.PLAYER.get().getName();
        this.prefix = this.getRank().getDisplayName();
        this.hyris = new HyrisMoney(this.uuid);
        this.hyode = new HyodeMoney(this.uuid);
        this.party = null;
        this.settings = (HyriPlayerSettings) HyriAPI.get().getPlayerSettingsManager().createPlayerSettings();
        this.cosmetics = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
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
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
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
    public IHyriMoney getMoney(String name) {
        if (name.equalsIgnoreCase(this.hyris.getName())) {
            return this.hyris;
        } else if (name.equalsIgnoreCase(this.hyode.getName())) {
            return this.hyode;
        }
        return null;
    }

    @Override
    public void setMoney(IHyriMoney money) {
        if (money instanceof HyrisMoney) {
            this.hyris = (HyrisMoney) money;
        } else if (money instanceof HyodeMoney) {
            this.hyode = (HyodeMoney) money;
        }
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
