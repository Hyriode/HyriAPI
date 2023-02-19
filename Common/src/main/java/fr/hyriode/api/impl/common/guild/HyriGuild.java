package fr.hyriode.api.impl.common.guild;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.guild.*;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 13/02/2023 at 00:17
 */
public class HyriGuild implements IHyriGuild, MongoSerializable, DataSerializable {

    @Expose
    private ObjectId _id = new ObjectId();

    @Expose
    private String name;

    @Expose
    private String tag;
    @Expose
    private HyriChatColor tagColor;

    @Expose
    private String description;
    @Expose
    private List<String> motd = new ArrayList<>();
    @Expose
    private String banner;

    @Expose
    private int slots = 10;

    @Expose
    private UUID leaderId;

    @Expose
    private final List<IGuildMember> members = new ArrayList<>();

    @Expose
    private final List<IGuildRank> ranks = new ArrayList<>();
    private final Map<Integer, GuildRank> ranksByPriority = new HashMap<>(); // Cache by priority

    private IGuildRank defaultRank;
    private IGuildRank leaderRank;

    @Expose
    private final GuildLeveling leveling = new GuildLeveling(this);
    @Expose
    private final GuildChest chest = new GuildChest();

    public HyriGuild() {}

    public HyriGuild(String name, UUID leaderId) {
        this.name = name;
        this.leaderId = leaderId;

        this.leaderRank = this.createRank("Chef", true);
        this.defaultRank = this.createRank("Membre");
        this.defaultRank.setDefault(true);

        this.addMember(this.leaderId).setRank(this.leaderRank.getName());
    }

    @Override
    public void save(MongoDocument document) {
        document.append("_id", this._id);
        document.append("name", this.name);
        document.append("tag", this.tag);
        document.append("description", this.description);
        document.append("motd", this.motd);
        document.append("banner", this.banner);
        document.append("slots", this.slots);
        document.append("leader", this.leaderId.toString());
        document.appendCollection("members", this.members, GuildMember.class);
        document.appendCollection("ranks", this.ranks, GuildRank.class);
        document.append("leveling", MongoSerializer.serialize(this.leveling));

        this.chest.save(document);
    }

    @Override
    public void load(MongoDocument document) {
        this._id = document.getObjectId("_id");
        this.name = document.getString("name");
        this.tag = document.getString("tag");
        this.description = document.getString("description");
        this.motd = document.getList("motd", String.class);
        this.banner = document.getString("banner");
        this.slots = document.getInteger("slots");
        this.leaderId = UUID.fromString(document.getString("leader"));

        final List<Document> membersDocuments = document.getList("members", Document.class);

        for (Document memberDocument : membersDocuments) {
            final GuildMember member = new GuildMember(this);

            member.load(MongoDocument.of(memberDocument));

            this.members.add(member);
        }

        final List<Document> ranksDocuments = document.getList("ranks", Document.class);

        for (Document rankDocument : ranksDocuments) {
            final GuildRank rank = new GuildRank(this);

            rank.load(MongoDocument.of(rankDocument));

            this.ranks.add(rank);
            this.ranksByPriority.put(rank.getPriority(), rank);

            if (rank.isDefault()) {
                this.defaultRank = rank;
            }

            if (rank.isLeader()) {
                this.leaderRank = rank;
            }
        }

        this.leveling.load(MongoDocument.of(document.get("leveling", Document.class)));
        this.chest.load(document);
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeString(this._id.toHexString());
        output.writeString(this.name);
        output.writeString(this.tag);
        output.writeString(this.description);
        output.writeInt(this.motd.size());

        for (String line : this.motd) {
            output.writeString(line);
        }

        output.writeString(this.banner);
        output.writeInt(this.slots);
        output.writeUUID(this.leaderId);
        output.writeInt(this.members.size());

        for (IGuildMember member : this.members) {
            ((GuildMember) member).write(output);
        }

        output.writeInt(this.ranks.size());

        for (IGuildRank rank : this.ranks) {
            ((GuildRank) rank).write(output);
        }

        this.leveling.write(output);
        this.chest.write(output);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this._id = new ObjectId(input.readString());
        this.name = input.readString();
        this.tag = input.readString();
        this.description = input.readString();

        final int motdSize = input.readInt();

        for (int i = 0; i < motdSize; i++) {
            this.motd.add(input.readString());
        }

        this.banner = input.readString();
        this.slots = input.readInt();
        this.leaderId = input.readUUID();

        final int membersSize = input.readInt();

        for (int i = 0; i < membersSize; i++) {
            final GuildMember member = new GuildMember(this);

            member.read(input);

            this.members.add(member);
        }

        final int ranksSize = input.readInt();

        for (int i = 0; i < ranksSize; i++) {
            final GuildRank rank = new GuildRank(this);

            rank.read(input);

            this.ranks.add(rank);
        }

        this.leveling.read(input);
        this.chest.read(input);
    }

    void setDefaultRank(IGuildRank defaultRank) {
        this.defaultRank.setDefault(false);
        this.defaultRank = defaultRank;
    }

    void increasePriority(GuildRank rank) {
        final int priority = rank.getPriority();

        if (priority == this.ranks.size() - 1) {
            return;
        }

        final int newPriority = priority + 1;

        rank.setPriority(newPriority);

        final GuildRank oldRank = this.ranksByPriority.put(newPriority, rank);

        if (oldRank != null) {
            oldRank.setPriority(priority);
        }
    }

    void decreasePriority(GuildRank rank) {
        final int priority = rank.getPriority();

        if (priority == 1) {
            return;
        }

        final int newPriority = priority - 1;

        rank.setPriority(newPriority);

        final GuildRank oldRank = this.ranksByPriority.put(newPriority, rank);

        if (oldRank != null) {
            oldRank.setPriority(priority);
        }
    }

    @Override
    public ObjectId getId() {
        return this._id;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public void setName(@NotNull String name) {
        if (!NAME_REGEX.matcher(name).find()) {
            throw new IllegalArgumentException("Name doesn't match the valid pattern!");
        }

        this.name = name;
    }

    @Override
    public @Nullable String getTag() {
        return this.tag;
    }

    @Override
    public void setTag(@Nullable String tag) {
        if (tag != null && !TAG_REGEX.matcher(tag).find()) {
            throw new IllegalArgumentException("Tag doesn't match the valid pattern!");
        }

        this.tag = tag;
    }

    @Override
    public @NotNull HyriChatColor getTagColor() {
        return this.tagColor;
    }

    @Override
    public void setTagColor(@NotNull HyriChatColor tagColor) {
        this.tagColor = tagColor;
    }

    @Override
    public @Nullable String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Override
    public @NotNull List<String> getMOTD() {
        return this.motd;
    }

    @Override
    public void setMOTD(@NotNull List<String> motd) {
        this.motd = motd;
    }

    @Override
    public void setMOTDLine(int line, @NotNull String value) {
        if (line <= 0 || this.motd.size() < line) {
            throw new IllegalArgumentException("Invalid line!");
        }

        this.motd.set(line, value);
    }

    @Override
    public void addMOTDLine(@NotNull String value) {
        if (this.motd.size() >= MAX_MOTD_LINES) {
            throw new IllegalStateException("MOTD size already exceeded!");
        }

        this.motd.add(value);
    }

    @Override
    public void clearMOTD() {
        this.motd.clear();
    }

    @Override
    public @Nullable String getBanner() {
        return this.banner;
    }

    @Override
    public void setBanner(@Nullable String banner) {
        this.banner = banner;
    }

    @Override
    public int getSlots() {
        return this.slots;
    }

    @Override
    public void addSlots(int slots) {
        this.slots += slots;
    }

    @Override
    public @NotNull IGuildMember getLeader() {
        return Objects.requireNonNull(this.getMember(this.leaderId));
    }

    @Override
    public void setLeader(@NotNull UUID leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public @NotNull List<IGuildMember> getMembers() {
        return this.members;
    }

    @Override
    public @Nullable IGuildMember getMember(@NotNull UUID memberId) {
        for (IGuildMember member : this.members) {
            if (member.getUniqueId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    @Override
    public @NotNull IGuildMember addMember(@NotNull UUID memberId) {
        if (this.getMember(memberId) != null) {
            throw new IllegalStateException("The player is already a member of the guild!");
        }

        final GuildMember member = new GuildMember(this, memberId, this.defaultRank.getName());

        this.members.add(member);

        return member;
    }

    @Override
    public void removeMember(@NotNull UUID memberId) {
        if (this.leaderId.equals(memberId)) {
            throw new IllegalStateException("Couldn't remove the leader!");
        }

        final IGuildMember member = this.getMember(memberId);

        if (member != null) {
            this.members.remove(member);
        }
    }

    @Override
    public boolean hasMember(@NotNull UUID memberId) {
        return this.getMember(memberId) != null;
    }

    @Override
    public @NotNull List<IGuildRank> getRanks() {
        return this.ranks;
    }

    @Override
    public @NotNull List<IGuildRank> getRanksSorted() {
        return this.ranks.stream().sorted((o1, o2) -> o2.isLeader() ? 1 : o2.getPriority() - o1.getPriority()).collect(Collectors.toList());
    }

    @Override
    public @Nullable IGuildRank getRank(@NotNull String rankName) {
        for (IGuildRank rank : this.ranks) {
            if (rank.getName().equals(rankName)) {
                return rank;
            }
        }
        return null;
    }

    @Override
    public @NotNull IGuildRank getDefaultRank() {
        return this.defaultRank;
    }

    @Override
    public @NotNull IGuildRank getLeaderRank() {
        return this.leaderRank;
    }

    @Override
    public @NotNull IGuildRank createRank(@NotNull String rankName) {
        return this.createRank(rankName, false);
    }

    private IGuildRank createRank(String rankName, boolean leader) {
        if (this.getRank(rankName) != null) {
            throw new IllegalStateException("'" + rankName + "' rank already exists!");
        }

        if (this.ranks.size() >= MAX_RANKS) {
            throw new IllegalStateException("Ranks limit already exceeded!");
        }

        final GuildRank rank = new GuildRank(this, rankName, leader, leader ? IGuildRank.LEADER_PRIORITY : this.ranks.size());

        this.ranks.add(rank);
        this.ranksByPriority.put(rank.getPriority(), rank);

        return rank;
    }

    @Override
    public void removeRank(@NotNull String rankName) {
        final IGuildRank rank = this.getRank(rankName);

        if (rank == null) {
            throw new IllegalStateException("Rank doesn't exist!");
        }

        if (rank.isLeader()) {
            throw new IllegalStateException("Couldn't remove the leader rank!");
        }

        if (rank.isDefault()) {
            throw new IllegalStateException("Couldn't remove the default rank!");
        }

        this.ranks.remove(rank);
        this.ranksByPriority.remove(rank.getPriority());

        // Re-calculate priorities
        for (GuildRank other : this.ranksByPriority.values()) {
            if (other.isLeader()) {
                continue;
            }

            if (other.getPriority() > rank.getPriority()) {
                other.setPriority(other.getPriority() - 1);
            }
        }

        // Re-set ranks
        for (IGuildMember member : this.members) {
            if (member.getRank().equals(rankName)) {
                member.setRank(this.defaultRank.getName());
            }
        }
    }

    @Override
    public @NotNull GuildLeveling getLeveling() {
        return this.leveling;
    }

    @Override
    public @NotNull IGuildChest getChest() {
        return this.chest;
    }

}
