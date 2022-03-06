package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.money.IHyriMoneyCallback;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public abstract class HyriMoney implements IHyriMoney {

    protected long amount;

    private final char colorChar;
    private final String name;

    private final UUID playerUUID;

    public HyriMoney(UUID playerUUID, String name, char colorChar) {
        this.playerUUID = playerUUID;
        this.name = name;
        this.colorChar = colorChar;
    }

    @Override
    public void add(long amount, String reason, boolean sendMessage, IHyriMoneyCallback callback) {
        HyriAPI.get().getMoneyManager().creditMoney(HyriAPI.get().getPlayerManager().getPlayer(this.playerUUID), HyriMoneyAction.ADD, this, amount, sendMessage, reason, callback);
    }

    @Override
    public void add(long amount, String reason, IHyriMoneyCallback callback) {
        this.add(amount, reason, true, callback);
    }

    @Override
    public void add(long amount, String reason, boolean sendMessage) {
        this.add(amount, reason, sendMessage, null);
    }

    @Override
    public void add(long amount, String reason) {
        this.add(amount, reason, true);
    }

    @Override
    public void add(long amount, boolean sendMessage, IHyriMoneyCallback callback) {
        this.add(amount, null, sendMessage, callback);
    }

    @Override
    public void add(long amount, IHyriMoneyCallback callback) {
        this.add(amount, true, callback);
    }

    @Override
    public void add(long amount, boolean sendMessage) {
        this.add(amount, null, sendMessage, null);
    }

    @Override
    public void add(long amount) {
        this.add(amount, true);
    }

    @Override
    public void remove(long amount, String reason, boolean sendMessage, IHyriMoneyCallback callback) {
        HyriAPI.get().getMoneyManager().creditMoney(HyriAPI.get().getPlayerManager().getPlayer(this.playerUUID), HyriMoneyAction.REMOVE, this, amount, sendMessage, reason, callback);
    }

    @Override
    public void remove(long amount, String reason, IHyriMoneyCallback callback) {
        this.remove(amount, reason, true, callback);
    }

    @Override
    public void remove(long amount, String reason, boolean sendMessage) {
        this.remove(amount, reason, sendMessage, null);
    }

    @Override
    public void remove(long amount, String reason) {
        this.remove(amount, reason, true);
    }

    @Override
    public void remove(long amount, boolean sendMessage, IHyriMoneyCallback callback) {
        this.remove(amount, null, sendMessage, callback);
    }

    @Override
    public void remove(long amount, IHyriMoneyCallback callback) {
        this.remove(amount, true, callback);
    }

    @Override
    public void remove(long amount, boolean sendMessage) {
        this.remove(amount, null, sendMessage, null);
    }

    @Override
    public void remove(long amount) {
        this.remove(amount, true);
    }

    @Override
    public boolean hasEnough(long amount) {
        return this.amount >= amount;
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
        return this.colorChar;
    }

}
