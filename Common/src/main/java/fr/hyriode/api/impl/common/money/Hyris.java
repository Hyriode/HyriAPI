package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class Hyris extends HyriMoney {

    public Hyris(UUID playerUUID) {
        super(playerUUID);
    }

    @Override
    public String getName() {
        return "Hyris";
    }

    @Override
    public HyriChatColor getColor() {
        return HyriChatColor.LIGHT_PURPLE;
    }

    @Override
    public long multiply(long currentAmount, IHyriPlayer account) {
        return (int) (currentAmount * this.getMultiplier(account));
    }

    @Override
    public double getMultiplier(IHyriPlayer account) {
        final Multiplier multiplier = Multiplier.getByPlayer(account);

        if (multiplier != null) {
            return multiplier.getAmount();
        }
        return 1.0D;
    }

    public enum Multiplier {

        PLAYER(HyriPlayerRankType.PLAYER, 1.0D),
        VIP(HyriPlayerRankType.VIP, 1.15D),
        VIP_PLUS(HyriPlayerRankType.VIP_PLUS, 1.5D),
        EPIC(HyriPlayerRankType.EPIC, 2.0D),
        HYRI_PLUS(HyriPlayerRankType.EPIC, 3.0D);

        private final HyriPlayerRankType rank;
        private final double amount;

        Multiplier(HyriPlayerRankType rank, double amount) {
            this.rank = rank;
            this.amount = amount;
        }

        public HyriPlayerRankType getRank() {
            return this.rank;
        }

        public double getAmount() {
            return this.amount;
        }

        public static Multiplier getByPlayer(IHyriPlayer account) {
            if (account.hasHyriPlus()) {
                return HYRI_PLUS;
            }
            for (Multiplier multiplier : values()) {
                if (multiplier.getRank() == account.getRank().getPlayerType()) {
                    return multiplier;
                }
            }
            return null;
        }

    }

}
