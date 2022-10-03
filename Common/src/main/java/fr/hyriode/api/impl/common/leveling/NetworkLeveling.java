package fr.hyriode.api.impl.common.leveling;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.leveling.event.HyriGainLevelEvent;
import fr.hyriode.api.leveling.event.HyriGainXPEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 12:42
 */
public class NetworkLeveling implements IHyriLeveling {

    public static final String LEADERBOARD_TYPE = "network-leveling";
    public static final String LEADERBOARD_NAME = "experience";

    public static final Algorithm ALGORITHM = new Algo();

    private final Set<Integer> claimedRewards = new HashSet<>();

    private final String name;
    private double experience;

    private transient UUID playerId;

    public NetworkLeveling(UUID playerId) {
        this.playerId = playerId;
        this.name = "network";
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getExperience() {
        return this.experience;
    }

    @Override
    public void setExperience(double experience) {
        this.runAction(() -> this.experience = experience);
    }

    @Override
    public double addExperience(double experience, boolean multipliers) {
        return this.runAction(() -> this.experience += multipliers ? this.applyMultiplier(experience) : experience);
    }

    @Override
    public void removeExperience(double experience) {
        this.runAction(() -> this.experience -= experience);
    }

    @Override
    public int getLevel() {
        return ALGORITHM.experienceToLevel(this.experience);
    }

    @Override
    public Algorithm getAlgorithm() {
        return ALGORITHM;
    }

    private double runAction(Runnable action) {
        final int oldLevel = this.getLevel();
        final double oldExperience = this.experience;

        action.run();

        final int newLevel = this.getLevel();
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(this.playerId);
        final IHyriEventBus eventBus = HyriAPI.get().getNetworkManager().getEventBus();

        if (oldExperience != this.experience) {
            HyriAPI.get().getLeaderboardProvider().getLeaderboard(LEADERBOARD_TYPE, LEADERBOARD_NAME).setScore(this.playerId, this.experience);

            eventBus.publish(new HyriGainXPEvent(account.getUniqueId(), this.name, oldExperience, this.experience));
        }

        if (newLevel > oldLevel) {
            eventBus.publish(new HyriGainLevelEvent(account.getUniqueId(), this.name, oldLevel, newLevel));
        }
        return this.experience - oldExperience;
    }

    @Override
    public double applyMultiplier(double experience) {
        final Multiplier multiplier = Multiplier.getByPlayer(IHyriPlayer.get(this.playerId));

        if (multiplier != null) {
            return experience * multiplier.getAmount();
        }
        return experience;
    }

    @Override
    public void claimReward(int level) {
        this.claimedRewards.add(level);

        Reward.get(level).claim(IHyriPlayer.get(this.playerId));
    }

    @Override
    public Set<Integer> getClaimedRewards() {
        return this.claimedRewards;
    }

    public static class Algo implements Algorithm {

        @Override
        public int experienceToLevel(double experience) {
            return (int) (350 + Math.sqrt(122500 + 1400 * experience)) / 700;
        }

        @Override
        public double levelToExperience(int level) {
            return 350 * level * level - 350 * level;
        }

    }

    private enum Multiplier {

        PLAYER(HyriPlayerRankType.PLAYER, 1.0D),
        VIP(HyriPlayerRankType.VIP, 1.25D),
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

    private enum Reward {

        LEVEL_1(1, player -> {
            player.getHyris().add(1000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_2(2, player -> {
            player.getHyris().add(1300)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_3(3, player -> {
            player.getHyris().add(1600)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_4(4, player -> {
            player.getHyris().add(2000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_5(5, player -> {
            // TODO Give a 1 star lootbox
        }),
        LEVEL_6(6, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_7(7, player -> {
            // TODO Give Aqua + color
        }),
        LEVEL_8(8, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_9(9, player -> {
            player.getHyris().add(2000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_10(10, player -> {
            // TODO Give 2 1 star lootbox
        }),
        LEVEL_11(11, player -> {

        }),
        LEVEL_12(12, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_13(13, player -> {

        }),
        LEVEL_14(14, player -> {
            // TODO Give White + color
        }),
        LEVEL_15(15, player -> {
            player.getHyris().add(1500)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_16(16, player -> {
            // TODO Give 4 1 star lootbox
        }),
        LEVEL_17(17, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_18(18, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_19(19, player -> {
            player.getHyris().add(4000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_20(20, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_21(21, player -> {
            // TODO Give Red + color
        }),
        LEVEL_22(22, player -> {
            // TODO Give a 2 star lootbox
        }),
        LEVEL_23(23, player -> {

        }),
        LEVEL_24(24, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_25(25, player -> {
            player.getHyris().add(5000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_26(26, player -> {

        }),
        LEVEL_27(27, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_28(28, player -> {
            // TODO Give Gray + color
        }),
        LEVEL_29(29, player -> {
            // TODO Give 2 2 star lootbox
        }),
        LEVEL_30(30, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_31(31, player -> {
            player.getHyris().add(6000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_32(32, player -> {

        }),
        LEVEL_33(33, player -> {

        }),
        LEVEL_34(34, player -> {
            // TODO Give a global booster
        }),
        LEVEL_35(35, player -> {
            // TODO Give Blue + color
        }),
        LEVEL_36(36, player -> {
            player.getHyris().add(7000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_37(37, player -> {
            // TODO Give 4 2 star lootbox
        }),
        LEVEL_38(38, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_39(39, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_40(40, player -> {
            player.setAvailableHosts(player.getAvailableHosts() + 1);
        }),
        LEVEL_41(41, player -> {
            player.getHyris().add(8000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_42(42, player -> {
            // TODO Give Yellow + color
        }),
        LEVEL_43(43, player -> {
            // TODO Give a 3 star lootbox
        }),
        LEVEL_44(44, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_45(45, player -> {

        }),
        LEVEL_46(46, player -> {
            player.getHyris().add(9000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_47(47, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_48(48, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_49(49, player -> {
            // TODO Give DarkGray + color
        }),
        LEVEL_50(50, player -> {
            // TODO Give 2 3 star lootbox
        }),
        LEVEL_51(51, player -> {

        }),
        LEVEL_52(52, player -> {
            player.getHyris().add(10000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_53(53, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_54(54, player -> {

        }),
        LEVEL_55(55, player -> {
            // TODO Give 4 3 star lootbox
        }),
        LEVEL_56(56, player -> {
            // TODO Give Green + color
        }),
        LEVEL_57(57, player -> {
            player.getHyris().add(10000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_58(58, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_59(59, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_60(60, player -> {

        }),
        LEVEL_61(61, player -> {
            // TODO Give a 4 star lootbox
        }),
        LEVEL_62(62, player -> {
            player.getHyris().add(10000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_63(63, player -> {
            // TODO Give a DarkRed + color
        }),
        LEVEL_64(64, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_65(65, player -> {
            player.setAvailableHosts(player.getAvailableHosts() + 1);
        }),
        LEVEL_66(66, player -> {

        }),
        LEVEL_67(67, player -> {
            player.getHyris().add(10000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_68(68, player -> {
            // TODO Give 2 4 star lootbox
        }),
        LEVEL_69(69, player -> {
            // TODO Give DarkBlue + color
        }),
        LEVEL_70(70, player -> {
            // TODO Give a global booster
        }),
        LEVEL_71(71, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_72(72, player -> {
            player.getHyris().add(11000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_73(73, player -> {

        }),
        LEVEL_74(74, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_75(75, player -> {
            // TODO Give 4 4 star lootbox
        }),
        LEVEL_76(76, player -> {
            // TODO Give DarkGreen + color
        }),
        LEVEL_77(77, player -> {
            player.getHyris().add(12500)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_78(78, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_79(79, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_80(80, player -> {

        }),
        LEVEL_81(81, player -> {
            player.getHyris().add(14000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_82(82, player -> {
            // TODO Give a 5 star lootbox
        }),
        LEVEL_83(83, player -> {
            // TODO Give DarkAqua + color
        }),
        LEVEL_84(84, player -> {

        }),
        LEVEL_85(85, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_86(86, player -> {
            player.setAvailableHosts(player.getAvailableHosts() + 1);
        }),
        LEVEL_87(87, player -> {
            player.getHyris().add(16000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_88(88, player -> {
            // TODO Give 2 5 star lootbox
        }),
        LEVEL_89(89, player -> {
            // TODO Give Dark + color
        }),
        LEVEL_90(90, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_91(91, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_92(92, player -> {
            player.getHyris().add(19000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_93(93, player -> {

        }),
        LEVEL_94(94, player -> {
            // Give Purple + color
        }),
        LEVEL_95(95, player -> {
            // TODO Give 4 5 star lootbox
        }),
        LEVEL_96(96, player -> {
            // TODO Give a selectable booster
        }),
        LEVEL_97(97, player -> {
            player.getHyris().add(25000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_98(98, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_99(99, player -> {
            // TODO Give Gold + color
        }),
        LEVEL_100(100, player -> {

        }),

        ;

        public static final Reward[] VALUES = values();

        private final int level;
        private final Consumer<IHyriPlayer> onClaim;

        Reward(int level, Consumer<IHyriPlayer> onClaim) {
            this.level = level;
            this.onClaim = onClaim;
        }

        public void claim(IHyriPlayer player) {
            this.onClaim.accept(player);
        }

        public int getLevel() {
            return this.level;
        }

        public static Reward get(int level) {
            for (Reward reward : VALUES) {
                if (reward.getLevel() == level) {
                    return reward;
                }
            }
            return null;
        }

    }

}
