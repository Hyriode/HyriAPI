package fr.hyriode.api.impl.common.player;

import com.google.gson.JsonElement;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.impl.common.leveling.NetworkLeveling;
import fr.hyriode.api.impl.common.money.Hyris;
import fr.hyriode.api.impl.common.player.nickname.HyriNickname;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettings;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.HyriPlayerData;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.nickname.HyriNicknameUpdatedEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.api.transaction.HyriTransaction;
import fr.hyriode.api.util.Skin;

import java.util.*;
import java.util.function.Predicate;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayer implements IHyriPlayer {

    private boolean online;

    private String name;
    private HyriNickname nickname;
    private final UUID uuid;

    private long lastLoginDate;
    private final long firstLoginDate;
    private long playTime;

    private HyriRank rank;
    private HyriPlus hyriPlus;
    private HyriChatColor plusColor = HyriChatColor.LIGHT_PURPLE;

    private UUID lastPrivateMessage;

    private final Hyris hyris;

    private UUID party;

    private HyriPlayerSettings settings;

    private String currentServer;
    private String lastServer;

    private String currentProxy;

    private boolean moderationMode;
    private boolean vanishMode;

    private final Map<String, JsonElement> statistics;
    private final Map<String, JsonElement> data;

    private final Map<String, List<JsonElement>> transactions;

    private final NetworkLeveling networkLeveling;

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
        this.settings = new HyriPlayerSettings();
        this.moderationMode = false;
        this.vanishMode = false;
        this.statistics = new HashMap<>();
        this.data = new HashMap<>();
        this.transactions = new HashMap<>();
        this.networkLeveling = new NetworkLeveling(this.uuid);
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
    public String getPrefix() {
        final String prefix = this.rank.getPrefix();

        if (this.rank.hasCustomPrefix()) {
            return prefix;
        } else if (this.rank.isStaff()) {
            return prefix;
        } else if (this.hasHyriPlus() && this.rank.getPlayerType() == HyriPlayerRankType.EPIC) {
            return prefix + this.plusColor.toString() + "+";
        }
        return prefix;
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
    public String getDisplayName() {
        return this.hasNickname() ? this.nickname.getName() : this.name;
    }

    @Override
    public IHyriNickname getNickname() {
        return this.nickname;
    }

    @Override
    public IHyriNickname createNickname(String name, String skinOwner, Skin skin) {
        return this.nickname = new HyriNickname(name, skinOwner, skin);
    }

    @Override
    public void setNickname(IHyriNickname nickname) {
        HyriAPI.get().getEventBus().publishAsync(new HyriNicknameUpdatedEvent(this, nickname));

        this.nickname = (HyriNickname) nickname;
    }

    @Override
    public String getNameWithRank(boolean nickname) {
        if (nickname && this.nickname != null) {
            final HyriPlayerRankType rank = this.nickname.getRank();

            return rank.getDefaultPrefix() + (rank.withSeparator() ? HyriRank.SEPARATOR : "") + rank.getDefaultColor().toString() + this.nickname.getName();
        }
        return HyriChatColor.translateAlternateColorCodes('&', this.getPrefix() + (this.rank.getType().withSeparator() ? HyriRank.SEPARATOR : "") + this.rank.getMainColor().toString() + this.getName());
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

        HyriAPI.get().getPlayerManager().savePrefix(this.uuid, this.getNameWithRank());
    }

    @Override
    public HyriPlus getHyriPlus() {
        if (this.rank.isStaff() || this.rank.is(HyriPlayerRankType.PARTNER)) {
            final long currentTime = System.currentTimeMillis();

            return new HyriPlus(currentTime, currentTime + 2629800000L);
        }
        return this.hyriPlus;
    }

    @Override
    public HyriChatColor getPlusColor() {
        return this.plusColor;
    }

    @Override
    public void setPlusColor(HyriChatColor plusColor) {
        this.plusColor = plusColor;
    }

    @Override
    public void setHyriPlus(HyriPlus hyriPlus) {
        this.hyriPlus = hyriPlus;
    }

    @Override
    public boolean hasHyriPlus() {
        if (this.rank.isStaff() || this.rank.is(HyriPlayerRankType.PARTNER)) {
            return true;
        }

        if (this.hyriPlus != null && this.hyriPlus.hasExpire()) {
            this.hyriPlus = null;
            return false;
        }
        return this.hyriPlus != null;
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
        return HyriAPI.get().getFriendManager().createHandler(this.uuid);
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

    @Override
    public List<String> getStatistics() {
        return new ArrayList<>(this.statistics.keySet());
    }

    @Override
    public <T extends HyriPlayerData> T getStatistics(String key, Class<T> statisticsClass) {
        final JsonElement serialized = this.statistics.get(key);

        if (serialized != null) {
            return HyriAPI.GSON.fromJson(serialized, statisticsClass);
        }
        return null;
    }

    @Override
    public void addStatistics(String key, HyriPlayerData statistics) {
        this.statistics.put(key, HyriAPI.GSON.toJsonTree(statistics));
    }

    @Override
    public void removeStatistics(String key) {
        this.statistics.remove(key);
    }

    @Override
    public boolean hasStatistics(String key) {
        return this.statistics.containsKey(key);
    }

    @Override
    public List<String> getData() {
        return new ArrayList<>(this.data.keySet());
    }

    @Override
    public <T extends HyriPlayerData> T getData(String key, Class<T> dataClass) {
        final JsonElement serialized = this.data.get(key);

        if (serialized != null) {
            return HyriAPI.GSON.fromJson(serialized, dataClass);
        }
        return null;
    }

    @Override
    public void addData(String key, HyriPlayerData data) {
        this.data.put(key, HyriAPI.GSON.toJsonTree(data));
    }

    @Override
    public void removeData(String key) {
        this.data.remove(key);
    }

    @Override
    public boolean hasData(String key) {
        return this.data.containsKey(key);
    }

    @Override
    public IHyriLeveling getNetworkLeveling() {
        this.networkLeveling.setPlayerId(this.uuid);

        return this.networkLeveling;
    }

    @Override
    public int getPriority() {
        if (this.rank.isStaff()) {
            return this.rank.getPriority();
        }
        return this.hasHyriPlus() ? HyriPlus.PRIORITY : this.rank.getPriority();
    }

    @Override
    public int getTabListPriority() {
        if (this.rank.isStaff()) {
            return this.rank.getTabListPriority();
        }
        return this.hasHyriPlus() ? HyriPlus.PRIORITY : this.rank.getTabListPriority();
    }

    @Override
    public <T extends HyriTransaction> T getTransaction(String type, String name, Class<T> transactionClass) {
        for (T transaction : this.getTransactions(type, transactionClass)) {
            if (transaction.getName().equals(name)) {
                return transaction;
            }
        }
        return null;
    }

    @Override
    public <T extends HyriTransaction> List<T> getTransactions(String type, Class<T> transactionsClass) {
        final List<JsonElement> elements = this.transactions.get(type);

        if (elements == null) {
            return null;
        }

        final List<T> transactions = new ArrayList<>();

        for (JsonElement element : elements) {
            transactions.add(HyriAPI.GSON.fromJson(element, transactionsClass));
        }
        return transactions;
    }

    @Override
    public void addTransaction(String type, HyriTransaction transaction) {
        final List<JsonElement> elements = this.transactions.getOrDefault(type, new ArrayList<>());

        elements.add(HyriAPI.GSON.toJsonTree(transaction));

        this.transactions.put(type, elements);
    }

    @Override
    public <T extends HyriTransaction> void removeTransaction(String type, String name, Class<T> transactionClass) {
        final List<T> transactions = this.getTransactions(type, transactionClass);

        if (transactions == null) {
            return;
        }

        transactions.removeIf(transaction -> transaction.getName().equals(name));

        final List<JsonElement> elements = new ArrayList<>();

        for (T transaction : transactions) {
            elements.add(HyriAPI.GSON.toJsonTree(transaction));
        }

        this.transactions.put(type, elements);
    }

    @Override
    public boolean hasTransaction(String type, String name, Class<? extends HyriTransaction> transactionClass) {
        for (HyriTransaction transaction : this.getTransactions(type, transactionClass)) {
            if (transaction.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getTransactionsTypes() {
        return new ArrayList<>(this.transactions.keySet());
    }

}