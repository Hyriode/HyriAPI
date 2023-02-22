package fr.hyriode.api.impl.common.player;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.impl.common.money.Gems;
import fr.hyriode.api.impl.common.money.Hyris;
import fr.hyriode.api.impl.common.player.model.HyriPlayerSettings;
import fr.hyriode.api.impl.common.player.model.HyriPlus;
import fr.hyriode.api.impl.common.player.model.HyriRank;
import fr.hyriode.api.impl.common.player.model.modules.*;
import fr.hyriode.api.leveling.NetworkLeveling;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlayerSettings;
import fr.hyriode.api.player.model.IHyriPlus;
import fr.hyriode.api.player.model.modules.*;
import fr.hyriode.api.rank.IHyriRank;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayer implements IHyriPlayer, MongoSerializable, DataSerializable {

    @Expose
    private UUID uuid;

    @Expose
    private String name;
    @Expose
    private final Set<String> nameHistory = new LinkedHashSet<>();

    @Expose
    private String lastIP;

    @Expose
    private long firstLoginDate;
    @Expose
    private long lastLoginDate;

    @Expose
    private HyriRank rank = new HyriRank(this);
    @Expose
    private HyriPlus hyriPlus = new HyriPlus(this);
    @Expose
    private HyriPlayerSettings settings = new HyriPlayerSettings(this);

    @Expose
    private final Hyris hyris = new Hyris(this);
    @Expose
    private final Gems gems = new Gems(this);
    @Expose
    private final NetworkLeveling networkLeveling = new NetworkLeveling(this);

    @Expose
    private ObjectId guild;

    @Expose
    private final HyriFriendsModule friends = new HyriFriendsModule(this);
    @Expose
    private final HyriPlayerHostModule hosts = new HyriPlayerHostModule(this);
    @Expose
    private final HyriAuthModule auth = new HyriAuthModule();
    @Expose
    private final HyriStatisticsModule statistics = new HyriStatisticsModule();
    @Expose
    private final HyriPlayerDataModule data = new HyriPlayerDataModule();
    @Expose
    private final HyriTransactionsModule transactions = new HyriTransactionsModule();

    public HyriPlayer() {}

    public HyriPlayer(boolean premium, String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.firstLoginDate = System.currentTimeMillis();
        this.nameHistory.add(name);
        this.auth.setPremium(premium);
    }

    @Override
    public void save(MongoDocument document) {
        document.append("_id", this.uuid.toString());
        document.append("name", this.name);
        document.append("name_history", this.nameHistory);
        document.append("last_ip", this.lastIP);
        document.append("first_login_date", this.firstLoginDate);
        document.append("last_login_date", this.lastLoginDate);
        document.append("rank", MongoSerializer.serialize(this.rank));
        document.append("hyri_plus", MongoSerializer.serialize(this.hyriPlus));
        document.append("settings", MongoSerializer.serialize(this.settings));
        document.append("hyris", this.hyris.getAmount());
        document.append("gems", this.gems.getAmount());
        document.append("network_leveling", MongoSerializer.serialize(this.networkLeveling));
        document.append("guild", this.guild);

        this.friends.save(document);

        document.append("auth", MongoSerializer.serialize(this.auth));
        document.append("hosts", MongoSerializer.serialize(this.hosts));
        document.append("statistics", MongoSerializer.serialize(this.statistics));

        this.data.save(document);

        document.append("transactions", MongoSerializer.serialize(this.transactions));
    }

    @Override
    public void load(MongoDocument document) {
        this.uuid = UUID.fromString(document.getString("_id"));
        this.name = document.getString("name");
        this.nameHistory.addAll(document.getList("name_history", String.class));
        this.lastIP = document.getString("last_ip");
        this.firstLoginDate = document.getLong("first_login_date");
        this.lastLoginDate = document.getLong("last_login_date");

        this.rank.load(MongoDocument.of(document.get("rank", Document.class)));
        this.hyriPlus.load(MongoDocument.of(document.get("hyri_plus", Document.class)));
        this.settings.load(MongoDocument.of(document.get("settings", Document.class)));

        this.hyris.setAmount(document.getLong("hyris"));
        this.gems.setAmount(document.getLong("gems"));

        this.networkLeveling.load(MongoDocument.of(document.get("network_leveling", Document.class)));

        this.guild = document.getObjectId("guild");

        this.friends.load(document);
        this.auth.load(MongoDocument.of(document.get("auth", Document.class)));
        this.hosts.load(MongoDocument.of(document.get("hosts", Document.class)));
        this.statistics.load(MongoDocument.of(document.get("statistics", Document.class)));
        this.data.load(document);
        this.transactions.load(MongoDocument.of(document.get("transactions", Document.class)));
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.uuid);
        output.writeString(this.name);
        output.writeInt(this.nameHistory.size());

        for (String name : this.nameHistory) {
            output.writeString(name);
        }

        output.writeString(this.lastIP);
        output.writeLong(this.firstLoginDate);
        output.writeLong(this.lastLoginDate);

        this.rank.write(output);
        this.hyriPlus.write(output);
        this.settings.write(output);

        output.writeLong(this.hyris.getAmount());
        output.writeLong(this.gems.getAmount());

        this.networkLeveling.write(output);

        output.writeString(this.guild == null ? null : this.guild.toHexString());

        this.auth.write(output);
        this.hosts.write(output);
        this.statistics.write(output);
        this.data.write(output);
        this.transactions.write(output);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.uuid = input.readUUID();
        this.name = input.readString();

        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            this.nameHistory.add(input.readString());
        }

        this.lastIP = input.readString();
        this.firstLoginDate = input.readLong();
        this.lastLoginDate = input.readLong();

        this.rank.read(input);
        this.hyriPlus.read(input);
        this.settings.read(input);

        this.hyris.setAmount(input.readLong());
        this.gems.setAmount(input.readLong());

        this.networkLeveling.read(input);

        final String guildId = input.readString();

        this.guild = guildId == null ? null : new ObjectId(guildId);

        this.auth.read(input);
        this.hosts.read(input);
        this.statistics.read(input);
        this.data.read(input);
        this.transactions.read(input);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if (this.name.equals(name)) {
            return;
        }

        this.name = name;
        this.nameHistory.add(this.name);
    }

    @Override
    public Set<String> getNameHistory() {
        if (this.nameHistory.size() == 0) {
            this.nameHistory.add(this.name);
        }
        return this.nameHistory;
    }

    @Override
    public String getPrefix() {
        final String prefix = this.rank.getPrefix();

        if (this.rank.isStaff()) {
            return prefix;
        }

        if (this.hyriPlus.has() && this.rank.getPlayerType() == PlayerRank.EPIC) {
            return prefix + this.hyriPlus.getColor() + "+";
        }
        return prefix;
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
    public @NotNull String getLastIP() {
        return this.lastIP;
    }

    @Override
    public void setLastIP(@NotNull String ip) {
        this.lastIP = ip;
    }

    @Override
    public @NotNull IHyriRank getRank() {
        return this.rank;
    }

    @Override
    public IHyriPlus getHyriPlus() {
        return this.hyriPlus;
    }

    @Override
    public IHyriMoney getHyris() {
        return this.hyris;
    }

    @Override
    public IHyriMoney getGems() {
        return this.gems;
    }

    @Override
    public NetworkLeveling getNetworkLeveling() {
        return this.networkLeveling;
    }

    @Override
    public int getPriority() {
        if (this.rank.isStaff()) {
            return this.rank.getPriority();
        }
        return this.hyriPlus.has() ? IHyriPlus.PRIORITY : this.rank.getPriority();
    }

    @Override
    public int getTabListPriority() {
        if (this.rank.isStaff()) {
            return this.rank.getTabListPriority();
        }
        return this.hyriPlus.has() ? IHyriPlus.TAB_LIST_PRIORITY : this.rank.getTabListPriority();
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
    public @NotNull ObjectId getGuild() {
        return this.guild;
    }

    @Override
    public void setGuild(@NotNull ObjectId guild) {
        this.guild = guild;
    }

    @Override
    public @NotNull IHyriFriendsModule getFriends() {
        return this.friends;
    }

    @Override
    public @NotNull IHyriAuthModule getAuth() {
        return this.auth;
    }

    @Override
    public @NotNull IHyriPlayerHostModule getHosts() {
        return this.hosts;
    }

    @Override
    public @NotNull IHyriStatisticsModule getStatistics() {
        return this.statistics;
    }

    @Override
    public @NotNull IHyriPlayerDataModule getData() {
        return this.data;
    }

    @Override
    public @NotNull IHyriTransactionsModule getTransactions() {
        return this.transactions;
    }

}
