package fr.hyriode.api.impl.common.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.impl.common.money.Hyode;
import fr.hyriode.api.impl.common.money.Hyris;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettings;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriRank;
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

    private String rank;

    private UUID lastPrivateMessage;

    private final Hyode hyode;
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
        this.rank = EHyriRank.PLAYER.getName();
        this.lastPrivateMessage = null;
        this.hyris = new Hyris(this.uuid);
        this.hyode = new Hyode(this.uuid);
        this.party = null;
        this.settings = (HyriPlayerSettings) HyriAPI.get().getPlayerSettingsManager().createPlayerSettings();
        this.moderationMode = this.getRank().getType().getId() >= EHyriRank.MODERATOR.getId();
        this.vanishMode = false;
    }

    @Override
    public boolean isOnline() {
        return this.online;
    }

    @Override
    public IHyriPlayer setOnline(boolean online) {
        this.online = online;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IHyriPlayer setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getCustomName() {
        return this.customName;
    }

    @Override
    public IHyriPlayer setCustomName(String customName) {
        this.customName = customName;
        return this;
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
    public IHyriPlayer setNameWithRank(String nameWithRank) {
        this.nameWithRank = nameWithRank;
        return this;
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
    public IHyriPlayer setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate.getTime();
        return this;
    }

    @Override
    public long getPlayTime() {
        return this.playTime;
    }

    @Override
    public IHyriPlayer setPlayTime(long playTime) {
        this.playTime = playTime;
        return this;
    }

    @Override
    public HyriRank getRank() {
        return HyriAPI.get().getRankManager().getRank(this.rank);
    }

    @Override
    public IHyriPlayer setRank(HyriRank rank) {
        this.rank = rank.getName();
        return this;
    }

    @Override
    public UUID getLastPrivateMessagePlayer() {
        return this.lastPrivateMessage;
    }

    @Override
    public IHyriPlayer setLastPrivateMessagePlayer(UUID player) {
        this.lastPrivateMessage = player;
        return this;
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
    public IHyriPlayer setParty(UUID party) {
        this.party = party;
        return this;
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
    public IHyriPlayer setSettings(IHyriPlayerSettings settings) {
        this.settings = (HyriPlayerSettings) settings;
        return this;
    }

    @Override
    public String getCurrentServer() {
        return this.currentServer;
    }

    @Override
    public IHyriPlayer setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
        return this;
    }

    @Override
    public String getLastServer() {
        return this.lastServer;
    }

    @Override
    public IHyriPlayer setLastServer(String lastServer) {
        this.lastServer = lastServer;
        return this;
    }

    @Override
    public String getCurrentProxy() {
        return this.currentProxy;
    }

    @Override
    public IHyriPlayer setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
        return this;
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
    public IHyriPlayer setInModerationMode(boolean inModerationMode) {
        this.moderationMode = inModerationMode;
        return this;
    }

    @Override
    public boolean isInVanishMode() {
        return this.vanishMode;
    }

    @Override
    public IHyriPlayer setInVanishMode(boolean inVanishMode) {
        this.vanishMode = inVanishMode;
        return this;
    }

}
