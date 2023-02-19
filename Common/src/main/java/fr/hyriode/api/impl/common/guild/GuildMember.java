package fr.hyriode.api.impl.common.guild;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.guild.IGuildMember;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 12/02/2023 at 22:49
 */
public class GuildMember implements IGuildMember, MongoSerializable, DataSerializable {

    @Expose
    private UUID uuid;
    @Expose
    private String rank;

    @Expose
    private double earnedExperience;
    @Expose
    private long depositedHyris;

    @Expose
    private long joinedDate;

    private final HyriGuild guild;

    protected GuildMember(HyriGuild guild) {
        this.guild = guild;
    }

    public GuildMember(HyriGuild guild, UUID uuid, String rank) {
        this.guild = guild;
        this.uuid = uuid;
        this.rank = rank;
        this.joinedDate = System.currentTimeMillis();
    }

    @Override
    public void save(MongoDocument document) {
        document.append("uuid", this.uuid.toString());
        document.append("rank", this.rank);
        document.append("earned_experience", this.earnedExperience);
        document.append("deposited_hyris", this.depositedHyris);
        document.append("joined_date", this.joinedDate);
    }

    @Override
    public void load(MongoDocument document) {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.rank = document.getString("rank");
        this.earnedExperience = document.getDouble("earned_experience");
        this.depositedHyris = document.getLong("deposited_hyris");
        this.joinedDate = document.getLong("joined_date");
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.uuid);
        output.writeString(this.rank);
        output.writeDouble(this.earnedExperience);
        output.writeLong(this.depositedHyris);
        output.writeLong(this.joinedDate);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.uuid = input.readUUID();
        this.rank = input.readString();
        this.earnedExperience = input.readDouble();
        this.depositedHyris = input.readLong();
        this.joinedDate = input.readLong();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public @NotNull String getRank() {
        return this.rank;
    }

    @Override
    public void setRank(@NotNull String rankName) {
        if (this.guild.getRank(rankName) == null) {
            throw new IllegalArgumentException("Invalid rank!");
        }

        if (!this.guild.getLeader().getUniqueId().equals(this.uuid) && rankName.equals(this.guild.getLeaderRank().getName())) {
            throw new IllegalStateException("Leader rank is already assigned to somebody else!");
        }

        this.rank = rankName;
    }

    @Override
    public double getEarnedExperience() {
        return this.earnedExperience;
    }

    @Override
    public void addEarnedExperience(double experience) {
        if (experience <= 0) {
            throw new IllegalArgumentException("Experience need to be greater than 0!");
        }

        this.earnedExperience += experience;
    }

    @Override
    public long getDepositedHyris() {
        return this.depositedHyris;
    }

    @Override
    public void addDepositedHyris(long hyris) {
        if (hyris <= 0) {
            throw new IllegalArgumentException("Hyris need to be greater than 0!");
        }

        this.depositedHyris += hyris;
    }

    @Override
    public long getJoinedDate() {
        return this.joinedDate;
    }


}
