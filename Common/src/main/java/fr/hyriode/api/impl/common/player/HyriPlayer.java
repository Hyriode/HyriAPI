package fr.hyriode.api.impl.common.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.impl.common.money.Hyris;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettings;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.settings.IHyriPlayerSettings;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayer implements IHyriPlayer {

    private boolean online;

    private String name;
    private String customName = null;
    private String nameWithRank = null;
    private final UUID uuid;

    private long lastLoginDate;
    private final long firstLoginDate;
    private long playTime;

    private HyriRank rank;
    private HyriPlus hyriPlus;

    private UUID lastPrivateMessage;

    private final Hyris hyris;

    private UUID party;

    private HyriPlayerSettings settings;

    private String currentServer;
    private String lastServer;

    private String currentProxy;

    private boolean moderationMode;
    private boolean vanishMode;

    public HyriPlayer(boolean online, String name, UUID uuid) {
        this.online = online;
        this.name = name;
        this.uuid = uuid;
        this.firstLoginDate = System.currentTimeMillis();
        this.lastLoginDate = this.firstLoginDate;
        this.rank = new HyriRank(HyriPlayerRankType.PLAYER);
        this.hyriPlus = null;
        this.lastPrivateMessage = null;
        this.hyris = new Hyris(this.uuid);
        this.party = null;
        this.settings = (HyriPlayerSettings) HyriAPI.get().getPlayerSettingsManager().createPlayerSettings();
        this.moderationMode = false;
        this.vanishMode = false;
    }

    @Override
    public boolean isOnline() {
        return this.online;
    }

    @Override
    public void setOnline(boolean online) {
        this.online = online;
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
    public String getNameWithRank() {
        return this.nameWithRank;
    }

    @Override
    public void setNameWithRank(String nameWithRank) {
        this.nameWithRank = nameWithRank;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public Date getFirstLoginDate() {
        return new Date(this.firstLoginDate);
    }

    @Override
    public Date getLastLoginDate() {
        return new Date(this.lastLoginDate);
    }

    @Override
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate.getTime();
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
        return this.rank;
    }

    @Override
    public void setRank(HyriRank rank) {
        this.rank = rank;
    }

    @Override
    public HyriPlus getHyriPlus() {
        return this.hyriPlus;
    }

    @Override
    public void setHyriPlus(HyriPlus hyriPlus) {
        this.hyriPlus = hyriPlus;
    }

    @Override
    public boolean hasHyriPlus() {
        return this.hyriPlus != null && !this.hyriPlus.hasExpire();
    }

    @Override
    public UUID getLastPrivateMessagePlayer() {
        return this.lastPrivateMessage;
    }

    @Override
    public void setLastPrivateMessagePlayer(UUID player) {
        this.lastPrivateMessage = player;
    }

    @Override
    public IHyriMoney getHyris() {
        return this.hyris;
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
    public String getCurrentServer() {
        return this.currentServer;
    }

    @Override
    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    @Override
    public String getLastServer() {
        return this.lastServer;
    }

    @Override
    public void setLastServer(String lastServer) {
        this.lastServer = lastServer;
    }

    @Override
    public String getCurrentProxy() {
        return this.currentProxy;
    }

    @Override
    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }

    @Override
    public IHyriFriendHandler getFriendHandler() {
        return HyriAPI.get().getFriendManager().loadFriends(this.uuid);
    }

    @Override
    public boolean isInModerationMode() {
        return this.moderationMode;
    }

    @Override
    public void setInModerationMode(boolean moderationMode) {
        this.moderationMode = moderationMode;
    }

    @Override
    public boolean isInVanishMode() {
        return this.vanishMode;
    }

    @Override
    public void setInVanishMode(boolean vanishMode) {
        this.vanishMode = vanishMode;
    }

}
