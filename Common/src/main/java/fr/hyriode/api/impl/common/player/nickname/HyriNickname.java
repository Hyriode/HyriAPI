package fr.hyriode.api.impl.common.player.nickname;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.nickname.HyriNicknameUpdatedEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.util.Skin;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 20:58
 */
public class HyriNickname implements IHyriNickname {

    private String name;
    private String skinOwner;
    private Skin skin;
    private HyriPlayerRankType rank;

    public HyriNickname(String name, String skinOwner, Skin skin) {
        this.name = name;
        this.skinOwner = skinOwner;
        this.skin = skin;
        this.rank = HyriPlayerRankType.PLAYER;
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
    public HyriPlayerRankType getRank() {
        return this.rank;
    }

    @Override
    public void setRank(HyriPlayerRankType rank) {
        this.rank = rank;
    }

    @Override
    public IHyriNickname update(UUID playerId) {
        HyriAPI.get().getEventBus().publishAsync(new HyriNicknameUpdatedEvent(playerId, this));
        return this;
    }

}
