package fr.hyriode.hyriapi.impl.api.player;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.api.money.model.HyodeMoney;
import fr.hyriode.hyriapi.impl.api.money.model.HyrisMoney;
import fr.hyriode.hyriapi.impl.api.settings.HyriPlayerSettings;
import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/07/2021 at 22:23
 */
public class HyriPlayer implements IHyriPlayer {

    private HyriPlayerSettings settings;

    private UUID party;

    private HyodeMoney hyode;
    private HyrisMoney hyris;

    private final UUID uuid;
    private final String name;

    public HyriPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.hyris = new HyrisMoney(this.uuid);
        this.hyode = new HyodeMoney(this.uuid);
        this.party = null;
        this.settings = (HyriPlayerSettings) HyriAPI.get().getPlayerSettingsManager().createPlayerSettings();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCustomName() {
        // TODO Nickname management
        return null;
    }

    @Override
    public String getDisplayName() {
        return this.hasNickname() ? this.getCustomName() : this.getName();
    }

    @Override
    public boolean hasNickname() {
        return this.getCustomName() != null;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public IHyriMoney getHyris() {
        return this.hyris;
    }

    @Override
    public IHyriMoney getHyode() {
        return this.hyode;
    }

    @Override
    public void setMoney(IHyriMoney money) {
        if (money instanceof HyrisMoney) {
            this.hyris = (HyrisMoney) money;
        } else if (money instanceof HyodeMoney) {
            this.hyris = (HyrisMoney) money;
        }
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

}
