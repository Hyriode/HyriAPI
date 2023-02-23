package fr.hyriode.api.impl.common.player.model;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlus;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 11:31
 */
public class HyriPlus implements IHyriPlus, MongoSerializable, DataSerializable {

    @Expose
    private long duration;
    @Expose
    private long enabledDate = -1;

    @Expose
    private HyriChatColor color = HyriChatColor.LIGHT_PURPLE;
    @Expose
    private final Set<HyriChatColor> colors = new HashSet<>(Collections.singletonList(HyriChatColor.LIGHT_PURPLE));

    private final IHyriPlayer player;

    public HyriPlus(IHyriPlayer player) {
        this.player = player;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("duration", this.duration);
        document.append("enabledDate", this.enabledDate);
        document.append("color", this.color.name());
        document.appendEnums("colors", this.colors);
    }

    @Override
    public void load(MongoDocument document) {
        this.duration = document.getLong("duration");
        this.enabledDate = document.getLong("enabledDate");
        this.color = HyriChatColor.valueOf(document.getString("color"));
        this.colors.addAll(document.getEnums("colors", HyriChatColor.class));
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeLong(this.duration);
        output.writeLong(this.enabledDate);
        output.writeChar(this.color.getChar());
        output.writeInt(this.colors.size());

        for (HyriChatColor color : this.colors) {
            output.writeChar(color.getChar());
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.duration = input.readLong();
        this.enabledDate = input.readLong();
        this.color = HyriChatColor.getByChar(input.readChar());

        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            this.colors.add(HyriChatColor.getByChar(input.readChar()));
        }
    }

    @Override
    public void enable() {
        this.enabledDate = System.currentTimeMillis();
    }

    @Override
    public boolean has() {
        if (this.player.getRank().isStaff() || this.player.getRank().is(PlayerRank.PARTNER)) {
            return true;
        }
        return !this.hasExpire();
    }

    @Override
    public boolean hasExpire() {
        final boolean result = System.currentTimeMillis() >= this.enabledDate + this.duration;

        if (result) {
            this.enabledDate = -1;
            this.duration = 0;
        }
        return result;
    }

    @Override
    public HyriChatColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(HyriChatColor color) {
        this.color = color;
    }

    @Override
    public Set<HyriChatColor> getColors() {
        return this.colors;
    }

    @Override
    public void addColor(HyriChatColor color) {
        this.colors.add(color);
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public long getEnabledDate() {
        return this.enabledDate;
    }

}
