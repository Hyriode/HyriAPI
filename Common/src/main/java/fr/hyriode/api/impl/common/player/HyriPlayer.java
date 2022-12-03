package fr.hyriode.api.impl.common.player;

import com.google.gson.JsonElement;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.impl.common.money.Gems;
import fr.hyriode.api.impl.common.money.Hyris;
import fr.hyriode.api.impl.common.settings.HyriPlayerSettings;
import fr.hyriode.api.impl.common.transaction.HyriTransaction;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.leveling.NetworkLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.HyriPlayerData;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.HyriRankUpdatedEvent;
import fr.hyriode.api.rank.hyriplus.HyriPlus;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.api.transaction.IHyriTransaction;
import fr.hyriode.api.transaction.IHyriTransactionContent;
import fr.hyriode.api.util.Skin;

import java.util.*;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayer implements IHyriPlayer {

    private UUID uuid;

    private String name;
    private boolean premium = true;

    private final long firstLoginDate = System.currentTimeMillis();
    private long lastLoginDate = this.firstLoginDate;
    private long playTime = 0;

    private HyriRank rank = new HyriRank();
    private HyriPlus hyriPlus = new HyriPlus();

    private int availableHosts = 0;
    private final List<UUID> playersBannedFromHost = new ArrayList<>();
    private final List<String> favoriteHostConfigs = new ArrayList<>();

    private final Hyris hyris = new Hyris(this.uuid);
    private final Gems gems = new Gems(this.uuid);

    private final NetworkLeveling networkLeveling = new NetworkLeveling(this.uuid);

    private HyriPlayerSettings settings = new HyriPlayerSettings();

    private final Map<String, JsonElement> statistics = new HashMap<>();
    private final Map<String, JsonElement> data = new HashMap<>();

    private final Map<String, List<HyriTransaction>> transactions = new HashMap<>();

    /**
     * Json serialization constructor.
     */
    private HyriPlayer() {}

    public HyriPlayer(boolean premium, String name, UUID uuid) {
        this.premium = premium;
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public boolean isPremium() {
        return this.premium;
    }

    @Override
    public boolean isOnline() {
        return IHyriPlayerSession.get(this.uuid) != null;
    }

    @Override
    public void setOnline(boolean online) {}

    @Override
    public String getPrefix() {
        final String prefix = this.rank.getPrefix();

        if (this.rank.hasCustomPrefix()) {
            return prefix;
        } else if (this.rank.isStaff()) {
            return prefix;
        } else if (this.hasHyriPlus() && this.rank.getPlayerType() == HyriPlayerRankType.EPIC) {
            return prefix + this.hyriPlus.getColor() + "+";
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
    public IHyriNickname getNickname() {
        return IHyriPlayerSession.get(this.uuid).getNickname();
    }

    @Override
    public IHyriNickname createNickname(String name, String skinOwner, Skin skin) {
        return IHyriPlayerSession.get(this.uuid).createNickname(name, skinOwner, skin);
    }

    @Override
    public void setNickname(IHyriNickname nickname) {
        IHyriPlayerSession.get(this.uuid).setNickname(nickname);
    }

    @Override
    public String getNameWithRank() {
        return HyriChatColor.translateAlternateColorCodes('&', this.getPrefix() + (this.rank.getType().withSeparator() ? HyriRank.SEPARATOR : "") + this.rank.getMainColor().toString() + this.getName());
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public long getFirstLoginDate() {
        return this.firstLoginDate;
    }

    @Override
    public long getLastLoginDate() {
        return this.lastLoginDate;
    }

    @Override
    public void setLastLoginDate(long lastLoginDate) {
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
        return this.rank;
    }

    @Override
    public void setRank(HyriRank rank) {
        this.rank = rank;

        HyriAPI.get().getEventBus().publish(new HyriRankUpdatedEvent(this.uuid));
    }

    @Override
    public HyriPlus getHyriPlus() {
        if (this.hyriPlus == null) {
            this.hyriPlus = new HyriPlus();
        }

        if (this.rank.isStaff() || this.rank.is(HyriPlayerRankType.PARTNER)) {
            this.hyriPlus.setDuration(1000L);
            this.hyriPlus.enable();
        }
        return this.hyriPlus;
    }

    @Override
    public boolean hasHyriPlus() {
        if (this.rank.isStaff() || this.rank.is(HyriPlayerRankType.PARTNER)) {
            return true;
        }
        return !this.getHyriPlus().hasExpire();
    }

    @Override
    public int getAvailableHosts() {
        return this.rank.isSuperior(HyriPlayerRankType.EPIC) ? 1 : this.availableHosts;
    }

    @Override
    public void setAvailableHosts(int availableHosts) {
        this.availableHosts = availableHosts;
    }

    @Override
    public List<UUID> getPlayersBannedFromHost() {
        return this.playersBannedFromHost;
    }

    @Override
    public void addPlayerBannedFromHost(UUID playerId) {
        this.playersBannedFromHost.add(playerId);
    }

    @Override
    public void removePlayerBannedFromHost(UUID playerId) {
        this.playersBannedFromHost.remove(playerId);
    }

    @Override
    public List<String> getFavoriteHostConfigs() {
        return this.favoriteHostConfigs;
    }

    @Override
    public void addFavoriteHostConfig(String configId) {
        this.favoriteHostConfigs.add(configId);
    }

    @Override
    public void removeFavoriteHostConfig(String configId) {
        this.favoriteHostConfigs.remove(configId);
    }

    @Override
    public UUID getLastPrivateMessagePlayer() {
        return IHyriPlayerSession.get(this.uuid).getPrivateMessageTarget();
    }

    @Override
    public void setLastPrivateMessagePlayer(UUID player) {
        IHyriPlayerSession.get(this.uuid).setPrivateMessageTarget(player);
    }

    @Override
    public IHyriMoney getHyris() {
        this.hyris.setPlayerUUID(this.uuid);

        return this.hyris;
    }

    @Override
    public IHyriMoney getGems() {
        this.gems.setPlayerUUID(this.uuid);

        return this.gems;
    }

    @Override
    public IHyriLeveling getNetworkLeveling() {
        this.networkLeveling.setPlayerId(this.uuid);

        return this.networkLeveling;
    }

    @Override
    public UUID getParty() {
        return IHyriPlayerSession.get(this.uuid).getParty();
    }

    @Override
    public void setParty(UUID party) {
        IHyriPlayerSession.get(this.uuid).setParty(party);
    }

    @Override
    public boolean hasParty() {
        return this.getParty() != null;
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
        return this.hasHyriPlus() ? HyriPlus.TAB_LIST_PRIORITY : this.rank.getTabListPriority();
    }

    @Override
    public IHyriPlayerSettings getSettings() {
        this.settings.providePlayerId(this.uuid);

        return this.settings;
    }

    @Override
    public void setSettings(IHyriPlayerSettings settings) {
        this.settings = (HyriPlayerSettings) settings;
    }

    @Override
    public String getCurrentServer() {
        return IHyriPlayerSession.get(this.uuid).getServer();
    }

    @Override
    public void setCurrentServer(String currentServer) {
        IHyriPlayerSession.get(this.uuid).setServer(currentServer);
    }

    @Override
    public String getLastServer() {
        return IHyriPlayerSession.get(this.uuid).getLastServer();
    }

    @Override
    public void setLastServer(String lastServer) {
        IHyriPlayerSession.get(this.uuid).setLastServer(lastServer);
    }

    @Override
    public String getCurrentProxy() {
        return IHyriPlayerSession.get(this.uuid).getProxy();
    }

    @Override
    public void setCurrentProxy(String currentProxy) {
        IHyriPlayerSession.get(this.uuid).setProxy(currentProxy);
    }

    @Override
    public IHyriFriendHandler getFriendHandler() {
        return HyriAPI.get().getFriendManager().createHandler(this.uuid);
    }

    @Override
    public boolean isInModerationMode() {
        return IHyriPlayerSession.get(this.uuid).isModerating();
    }

    @Override
    public void setInModerationMode(boolean moderationMode) {
        IHyriPlayerSession.get(this.uuid).setModerating(moderationMode);
    }

    @Override
    public boolean isInVanishMode() {
        return IHyriPlayerSession.get(this.uuid).isVanished();
    }

    @Override
    public void setInVanishMode(boolean vanishMode) {
        IHyriPlayerSession.get(this.uuid).setVanished(vanishMode);
    }

    @Override
    public List<String> getStatistics() {
        return Collections.unmodifiableList(new ArrayList<>(this.statistics.keySet()));
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
        return Collections.unmodifiableList(new ArrayList<>(this.data.keySet()));
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
    public boolean addTransaction(String type, String name, IHyriTransactionContent content) {
        if (this.hasTransaction(type, name)) {
            return false;
        }

        final List<HyriTransaction> transactions = this.transactions.getOrDefault(type, new ArrayList<>());

        transactions.add(new HyriTransaction(name, System.currentTimeMillis(), content));

        this.transactions.put(type, transactions);

        return true;
    }

    @Override
    public boolean removeTransaction(String type, String name) {
        final List<HyriTransaction> transactions = this.transactions.getOrDefault(type, new ArrayList<>());

        if (transactions == null) {
            return false;
        }

        final HyriTransaction transaction = this.getTransaction(type, name);

        if (transaction == null) {
            return false;
        }

        transactions.remove(transaction);

        this.transactions.put(type, transactions);

        return true;
    }

    @Override
    public HyriTransaction getTransaction(String type, String name) {
        final List<HyriTransaction> transactions = this.transactions.get(type);

        if (transactions == null) {
            return null;
        }

        for (final HyriTransaction transaction : transactions) {
            if (transaction.name().equals(name)) {
                return transaction;
            }
        }
        return null;
    }

    @Override
    public boolean hasTransaction(String type, String name) {
        final List<? extends IHyriTransaction> transactions = this.transactions.get(type);

        if (transactions == null) {
            return false;
        }

        for (final IHyriTransaction transaction : transactions) {
            if (transaction.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<HyriTransaction> getTransactions(String type) {
        return this.transactions.get(type);
    }

    @Override
    public Map<String, List<HyriTransaction>> getTransactions() {
        return Collections.unmodifiableMap(this.transactions);
    }

    @Override
    public List<String> getTransactionsTypes() {
        return Collections.unmodifiableList(new ArrayList<>(this.transactions.keySet()));
    }

}
