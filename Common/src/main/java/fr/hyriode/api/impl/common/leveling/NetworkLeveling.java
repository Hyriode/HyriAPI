package fr.hyriode.api.impl.common.leveling;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.HyriBoosterTransaction;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.leveling.event.HyriGainLevelEvent;
import fr.hyriode.api.leveling.event.HyriGainXPEvent;
import fr.hyriode.api.lootbox.HyriLootboxRarity;
import fr.hyriode.api.lootbox.HyriLootboxTransaction;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.hyriplus.HyriPlusTransaction;
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

        final Reward reward = Reward.get(level);

        if (reward == null) {
            return;
        }

        reward.claim(IHyriPlayer.get(this.playerId));
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
            player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.ONE_STAR));
        }),
        LEVEL_6(6, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_7(7, player -> {
            player.getHyriPlus().addColor(HyriChatColor.AQUA);
        }),
        LEVEL_8(8, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 1.75D, 3600);
        }),
        LEVEL_9(9, player -> {
            player.getHyris().add(2000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_10(10, player -> {
            for (int i = 0; i < 2; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.ONE_STAR));
            }
        }),
        LEVEL_11(11, player -> {

        }),
        LEVEL_12(12, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 1.75D, 3600);
        }),
        LEVEL_13(13, player -> {

        }),
        LEVEL_14(14, player -> {
            player.getHyriPlus().addColor(HyriChatColor.WHITE);
        }),
        LEVEL_15(15, player -> {
            player.getHyris().add(1500)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_16(16, player -> {
            for (int i = 0; i < 4; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.ONE_STAR));
            }
        }),
        LEVEL_17(17, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 1.75D, 3600);
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
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 1.75D, 3600);
        }),
        LEVEL_21(21, player -> {
            player.getHyriPlus().addColor(HyriChatColor.RED);
        }),
        LEVEL_22(22, player -> {
            player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.TWO_STARS));
        }),
        LEVEL_23(23, player -> {

        }),
        LEVEL_24(24, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 1.75D, 3600);
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
            player.getHyriPlus().addColor(HyriChatColor.GRAY);
        }),
        LEVEL_29(29, player -> {
            for (int i = 0; i < 2; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.TWO_STARS));
            }
        }),
        LEVEL_30(30, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 1.75D, 3600);
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
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.GLOBAL_TYPE, 1.75D, 3600);
        }),
        LEVEL_35(35, player -> {
            player.getHyriPlus().addColor(HyriChatColor.BLUE);
        }),
        LEVEL_36(36, player -> {
            player.getHyris().add(7000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_37(37, player -> {
            for (int i = 0; i < 4; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.TWO_STARS));
            }
        }),
        LEVEL_38(38, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_39(39, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.25D, 3600);
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
            player.getHyriPlus().addColor(HyriChatColor.YELLOW);
        }),
        LEVEL_43(43, player -> {
            player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.THREE_STARS));
        }),
        LEVEL_44(44, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.25D, 3600);
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
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.25D, 3600);
        }),
        LEVEL_49(49, player -> {
            player.getHyriPlus().addColor(HyriChatColor.DARK_GRAY);
        }),
        LEVEL_50(50, player -> {
            for (int i = 0; i < 2; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.THREE_STARS));
            }
        }),
        LEVEL_51(51, player -> {

        }),
        LEVEL_52(52, player -> {
            player.getHyris().add(10000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_53(53, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.25D, 3600);
        }),
        LEVEL_54(54, player -> {

        }),
        LEVEL_55(55, player -> {
            for (int i = 0; i < 4; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.THREE_STARS));
            }
        }),
        LEVEL_56(56, player -> {
            player.getHyriPlus().addColor(HyriChatColor.GREEN);
        }),
        LEVEL_57(57, player -> {
            player.getHyris().add(10000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_58(58, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.25D, 3600);
        }),
        LEVEL_59(59, player -> {
            // TODO Give a cosmetic
        }),
        LEVEL_60(60, player -> {

        }),
        LEVEL_61(61, player -> {
            player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.FOUR_STARS));
        }),
        LEVEL_62(62, player -> {
            player.getHyris().add(10000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_63(63, player -> {
            player.getHyriPlus().addColor(HyriChatColor.DARK_RED);
        }),
        LEVEL_64(64, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.25D, 3600);
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
            for (int i = 0; i < 2; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.FOUR_STARS));
            }
        }),
        LEVEL_69(69, player -> {
            player.getHyriPlus().addColor(HyriChatColor.DARK_BLUE);
        }),
        LEVEL_70(70, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.GLOBAL_TYPE, 2.25D, 3600);
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
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.75D, 3600);
        }),
        LEVEL_75(75, player -> {
            for (int i = 0; i < 4; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.FOUR_STARS));
            }
        }),
        LEVEL_76(76, player -> {
            player.getHyriPlus().addColor(HyriChatColor.DARK_GREEN);
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
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.75D, 3600);
        }),
        LEVEL_80(80, player -> {

        }),
        LEVEL_81(81, player -> {
            player.getHyris().add(14000)
                    .withMultiplier(false)
                    .exec();
        }),
        LEVEL_82(82, player -> {
            player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.FIVE_STARS));
        }),
        LEVEL_83(83, player -> {
            player.getHyriPlus().addColor(HyriChatColor.DARK_AQUA);
        }),
        LEVEL_84(84, player -> {

        }),
        LEVEL_85(85, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.75D, 3600);
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
            for (int i = 0; i < 2; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.FIVE_STARS));
            }
        }),
        LEVEL_89(89, player -> {
            player.getHyriPlus().addColor(HyriChatColor.BLACK);
        }),
        LEVEL_90(90, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.SELECTABLE_TYPE, 2.75D, 3600);
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
            player.getHyriPlus().addColor(HyriChatColor.DARK_PURPLE);
        }),
        LEVEL_95(95, player -> {
            for (int i = 0; i < 4; i++) {
                player.addTransaction(HyriLootboxTransaction.TYPE, new HyriLootboxTransaction(HyriLootboxRarity.FIVE_STARS));
            }
        }),
        LEVEL_96(96, player -> {
            HyriAPI.get().getBoosterManager().giveBooster(player, IHyriBoosterManager.GLOBAL_TYPE, 2.75D, 3600);
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
            player.getHyriPlus().addColor(HyriChatColor.GOLD);
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
