package fr.hyriode.api.impl.common.money;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.PlayerRank;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class Hyris extends HyriMoney {

    public Hyris(IHyriPlayer player) {
        super(player);
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
    public double getMultiplier(IHyriPlayer account) {
        final Multiplier multiplier = Multiplier.getByPlayer(account);

        if (multiplier != null) {
            return multiplier.getAmount();
        }
        return 1.0D;
    }

    public enum Multiplier {

        PLAYER(PlayerRank.PLAYER, 1.0D),
        VIP(PlayerRank.VIP, 1.50D),
        VIP_PLUS(PlayerRank.VIP_PLUS, 1.75D),
        EPIC(PlayerRank.EPIC, 2.0D),
        HYRI_PLUS(PlayerRank.EPIC, 2.50D);

        private final PlayerRank rank;
        private final double amount;

        Multiplier(PlayerRank rank, double amount) {
            this.rank = rank;
            this.amount = amount;
        }

        public PlayerRank getRank() {
            return this.rank;
        }

        public double getAmount() {
            return this.amount;
        }

        public static Multiplier getByPlayer(IHyriPlayer account) {
            if (account.getHyriPlus().has()) {
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
