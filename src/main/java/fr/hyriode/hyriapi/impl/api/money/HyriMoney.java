package fr.hyriode.hyriapi.impl.api.money;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.money.IHyriMoney;
import fr.hyriode.hyriapi.money.IHyriMoneyCallback;
import fr.hyriode.hyriapi.money.IHyriMoneyManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 22:08
 */
public abstract class HyriMoney implements IHyriMoney {

    protected long amount;

    private final ChatColor color;
    private final String name;

    private final UUID playerUUID;

    public HyriMoney(UUID playerUUID, String name, ChatColor color) {
        this.playerUUID = playerUUID;
        this.name = name;
        this.color = color;
    }

    @Override
    public void add(long amount, String reason, IHyriMoneyCallback callback) {
        HyriAPI.get().getMoneyManager().creditMoney(HyriAPI.get().getPlayerManager().getPlayer(this.playerUUID), HyriMoneyAction.ADD, this, amount, reason, callback);
    }

    @Override
    public void add(long amount, String reason) {
        this.add(amount, reason, null);
    }

    @Override
    public void add(long amount, IHyriMoneyCallback callback) {
        this.add(amount, null, callback);
    }

    @Override
    public void add(long amount) {
        this.add(amount, null, null);
    }

    @Override
    public void remove(long amount, String reason, IHyriMoneyCallback callback) {
        HyriAPI.get().getMoneyManager().creditMoney(HyriAPI.get().getPlayerManager().getPlayer(this.playerUUID), HyriMoneyAction.REMOVE, this, amount, reason, callback);
    }

    @Override
    public void remove(long amount, String reason) {
        this.remove(amount, reason, null);
    }

    @Override
    public void remove(long amount, IHyriMoneyCallback callback) {
        this.remove(amount, null, callback);
    }

    @Override
    public void remove(long amount) {
        this.remove(amount, null, null);
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public char getColorChar() {
        return this.color.getChar();
    }

}
