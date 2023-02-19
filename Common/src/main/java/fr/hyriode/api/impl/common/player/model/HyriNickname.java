package fr.hyriode.api.impl.common.player.model;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.event.NicknameUpdatedEvent;
import fr.hyriode.api.player.model.IHyriNickname;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import fr.hyriode.api.util.Skin;

import java.io.IOException;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 20:58
 */
public class HyriNickname implements IHyriNickname, DataSerializable {

    @Expose
    private String name;
    @Expose
    private String skinOwner;
    @Expose
    private Skin skin;
    @Expose
    private int rank = -1;

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeString(this.name);
        output.writeString(this.skinOwner);
        output.writeString(this.skin.getTextureData());
        output.writeString(this.skin.getTextureSignature());
        output.writeInt(this.rank);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.name = input.readString();
        this.skinOwner = input.readString();
        this.skin = new Skin(input.readString(), input.readString());
        this.rank = input.readInt();
    }

    @Override
    public boolean has() {
        return this.name != null && !this.name.isEmpty() && this.rank > -1;
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
    public String getSkinOwner() {
        return this.skinOwner;
    }

    @Override
    public void setSkinOwner(String skinOwner) {
        this.skinOwner = skinOwner;
    }

    @Override
    public Skin getSkin() {
        return this.skin;
    }

    @Override
    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    @Override
    public PlayerRank getRank() {
        return this.rank == -1 ? null : PlayerRank.getById(this.rank);
    }

    @Override
    public void setRank(PlayerRank rank) {
        this.rank = rank.getId();
    }

    @Override
    public void update(UUID playerId) {
        HyriAPI.get().getEventBus().publishAsync(new NicknameUpdatedEvent(playerId, this));
    }

}
