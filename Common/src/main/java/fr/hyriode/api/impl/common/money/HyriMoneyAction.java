package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.money.IHyriMoneyAction;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/05/2022 at 07:24
 */
public class HyriMoneyAction implements IHyriMoneyAction {

    private long amount;
    private String reason;
    private boolean multiplier = true;
    private boolean message;

    private final Type type;
    private final UUID player;
    private final IHyriMoney money;

    public HyriMoneyAction(Type type, UUID player, long amount, IHyriMoney money) {
        this.type = type;
        this.player = player;
        this.amount = amount;
        this.money = money;
    }

    @Override
    public IHyriMoneyAction withAmount(long amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public IHyriMoneyAction withReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public IHyriMoneyAction withMultiplier(boolean multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    @Override
    public boolean isMultiplier() {
        return this.multiplier;
    }

    @Override
    public IHyriMoneyAction withMessage(boolean message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean isMessage() {
        return this.message;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public long exec() {
        return HyriAPI.get().getMoneyManager().creditMoney(this.player, this, this.money);
    }

}
