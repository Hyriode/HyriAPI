package fr.hyriode.api.impl.common.money;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.money.IHyriMoneyAction;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public abstract class HyriMoney implements IHyriMoney {

    @Expose
    protected long amount;

    protected IHyriPlayer player;

    public HyriMoney(IHyriPlayer player) {
        this.player = player;
    }

    @Override
    public IHyriMoneyAction add(long amount) {
        return new HyriMoneyAction(IHyriMoneyAction.Type.ADD, this.player.getUniqueId(), amount, this);
    }

    @Override
    public IHyriMoneyAction remove(long amount) {
        return new HyriMoneyAction(IHyriMoneyAction.Type.REMOVE, this.player.getUniqueId(), amount, this);
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

}
