package fr.hyriode.api.impl.common.guild;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fr.hyriode.api.guild.IGuildRank;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by AstFaster
 * on 12/02/2023 at 23:00
 */
public class GuildRank implements IGuildRank, MongoSerializable, DataSerializable {

    @Expose
    private String name;
    @Expose
    private String tag;

    @Expose
    @SerializedName(value = "default")
    private boolean _default;
    @Expose
    private boolean leader;

    @Expose
    private int priority;
    @Expose
    private long createdDate;

    private final HyriGuild guild;

    GuildRank(HyriGuild guild) {
        this.guild = guild;
    }

    public GuildRank(HyriGuild guild, String name, boolean leader, int priority) {
        this.guild = guild;
        this.name = name;
        this.leader = leader;
        this.priority = priority;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("name", this.name);
        document.append("tag", this.tag);
        document.append("default", this._default);
        document.append("leader", this.leader);
        document.append("priority", this.priority);
        document.append("created_date", this.createdDate);
    }

    @Override
    public void load(MongoDocument document) {
        this.name = document.getString("name");
        this.tag = document.getString("tag");
        this._default = document.getBoolean("default");
        this.leader = document.getBoolean("leader");
        this.priority = document.getInteger("priority");
        this.createdDate = document.getLong("created_date");
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeString(this.name);
        output.writeString(this.tag);
        output.writeBoolean(this._default);
        output.writeBoolean(this.leader);
        output.writeInt(this.priority);
        output.writeLong(this.createdDate);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.name = input.readString();
        this.tag = input.readString();
        this._default = input.readBoolean();
        this.leader = input.readBoolean();
        this.priority = input.readInt();
        this.createdDate = input.readLong();
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public void setName(@NotNull String name) {
        if (this.guild.getRank(name) != null) {
            throw new IllegalArgumentException("A rank already exists with this name!");
        }

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
        for (IGuildRank rank : this.guild.getRanks()) {
            if (Objects.equals(rank.getTag(), tag)) {
                throw new IllegalArgumentException("A rank already exists with this tag!");
            }
        }

        if (tag != null && !TAG_REGEX.matcher(tag).find()) {
            throw new IllegalArgumentException("Tag doesn't match the valid pattern!");
        }

        this.tag = tag;
    }

    @Override
    public boolean isDefault() {
        return this._default;
    }

    @Override
    public void setDefault(boolean _default) {
        if (this.leader) {
            throw new IllegalStateException("Couldn't set the leader rank as default!");
        }

        this._default = _default;

        if (this._default) {
            this.guild.setDefaultRank(this);
        }
    }

    @Override
    public boolean isLeader() {
        return this.leader;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int increasePriority() {
        this.guild.increasePriority(this);

        return this.priority;
    }

    @Override
    public int decreasePriority() {
        this.guild.decreasePriority(this);

        return this.priority;
    }

    @Override
    public long getCreatedDate() {
        return this.createdDate;
    }

}
