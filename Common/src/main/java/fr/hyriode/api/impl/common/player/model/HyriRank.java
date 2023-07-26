package fr.hyriode.api.impl.common.player.model;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.event.RankUpdatedEvent;
import fr.hyriode.api.rank.IHyriRank;
import fr.hyriode.api.rank.IHyriRankType;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.api.rank.StaffRank;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import fr.hyriode.hyreos.api.HyreosRedisKey;

import java.io.IOException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:00
 */
public class HyriRank implements IHyriRank, MongoSerializable, DataSerializable {

    @Expose
    private String prefix;
    @Expose
    private HyriChatColor mainColor;

    @Expose
    private PlayerRank playerType = PlayerRank.PLAYER;
    @Expose
    private StaffRank staffType;

    private final IHyriPlayer player;

    public HyriRank(IHyriPlayer player) {
        this.player = player;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("prefix", this.prefix);
        document.append("mainColor", this.mainColor == null ? null : this.mainColor.name());
        document.append("playerType", this.playerType.name());
        document.append("staffType", this.staffType == null ? null : this.staffType.name());
    }

    @Override
    public void load(MongoDocument document) {
        this.prefix = document.getString("prefix");

        final String mainColor = document.getString("mainColor");

        this.mainColor = mainColor == null ? null : HyriChatColor.valueOf(mainColor);
        this.playerType = PlayerRank.valueOf(document.getString("playerType"));

        final String staffType = document.getString("staffType");

        this.staffType = staffType == null ? null : StaffRank.valueOf(staffType);
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeString(this.prefix);
        output.writeString(this.mainColor == null ? null : this.mainColor.name());
        output.writeInt(this.playerType.getId());
        output.writeInt(this.staffType == null ? -1 : this.staffType.getId());
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.prefix = input.readString();

        final String mainColor = input.readString();

        this.mainColor = mainColor == null ? null : HyriChatColor.valueOf(mainColor);
        this.playerType = PlayerRank.getById(input.readInt());
        this.staffType = StaffRank.getById(input.readInt());
    }

    @Override
    public boolean is(PlayerRank playerType) {
        return this.getPlayerType().getId() == playerType.getId();
    }

    @Override
    public boolean is(StaffRank staffType) {
        return this.staffType != null && this.staffType.getId() == staffType.getId();
    }

    @Override
    public boolean isSuperior(PlayerRank playerType) {
        return this.getPlayerType().getId() >= playerType.getId();
    }

    @Override
    public boolean isSuperior(StaffRank staffType) {
        return this.staffType != null && this.staffType.getId() >= staffType.getId();
    }

    @Override
    public String getPrefix() {
        return this.prefix != null ? this.prefix : (this.isStaff() ? this.staffType.getDefaultPrefix() : this.playerType.getDefaultPrefix());
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean hasCustomPrefix() {
        return this.prefix != null;
    }

    @Override
    public HyriChatColor getMainColor() {
        return this.mainColor != null ? this.mainColor : (this.isStaff() ? this.staffType.getDefaultColor() : this.playerType.getDefaultColor());
    }

    @Override
    public void setMainColor(HyriChatColor mainColor) {
        this.mainColor = mainColor;
    }

    @Override
    public boolean withSeparator() {
        return this.getType().withSeparator();
    }

    @Override
    public IHyriRankType getType() {
        return this.isStaff() ? this.staffType : this.playerType;
    }

    @Override
    public PlayerRank getPlayerType() {
        return this.isStaff() ? PlayerRank.PARTNER : this.playerType;
    }

    @Override
    public PlayerRank getRealPlayerType() {
        return this.playerType;
    }

    @Override
    public void setPlayerType(PlayerRank playerType) {
        final PlayerRank old = this.playerType;
        this.playerType = playerType;

        HyriAPI.get().getEventBus().publish(new RankUpdatedEvent(this.player.getUniqueId()));

        final String key = String.format(HyreosRedisKey.RANKS.getKey(), playerType.getName());
        final String oldKey = String.format(HyreosRedisKey.RANKS.getKey(), old.getName());
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            jedis.decr(oldKey);
            jedis.incr(key);
        });
    }

    @Override
    public StaffRank getStaffType() {
        return this.staffType;
    }

    @Override
    public void setStaffType(StaffRank staffType) {
        this.staffType = staffType;

//        HyriAPI.get().getEventBus().publish(new RankUpdatedEvent(this.player.getUniqueId()));
    }

    @Override
    public boolean isDefault() {
        return this.playerType == PlayerRank.PLAYER && !this.isStaff();
    }

    @Override
    public boolean isStaff() {
        return this.staffType != null;
    }

    @Override
    public int getPriority() {
        return this.getType().getPriority();
    }

    @Override
    public int getTabListPriority() {
        return this.getType().getTabListPriority();
    }

}