package fr.hyriode.api.leveling;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.leveling.event.HyriGainLevelEvent;
import fr.hyriode.api.leveling.event.HyriGainXPEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 12:42
 */
public class NetworkLeveling implements IHyriLeveling {

    public static final String LEADERBOARD_TYPE = "network-leveling";
    public static final String LEADERBOARD_NAME = "experience";

    public static final Algorithm ALGORITHM = new Algo();

    private Set<Integer> claimedRewards = new HashSet<>();

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
    }

    @Override
    public Set<Integer> getClaimedRewards() {
        return this.claimedRewards == null ? this.claimedRewards = new HashSet<>() : this.claimedRewards;
    }

    public static class Algo implements Algorithm {

        @Override
        public int experienceToLevel(double experience) {
            final double tolerance = 1e-100; // Define a tolerance for the solution
            double x0 = 1; // Define the initial guess for the solution

            while (true) { // Use Newton's method to find the root of the equation; f(g(x)) - x = 0
                final double x1 = x0 - ((Math.ceil(180 * x0 * Math.sqrt(x0) + 300)) - experience) / (180 * Math.sqrt(x0));

                if (Math.abs(x1 - x0) < tolerance) {
                    return (int) Math.ceil(x1);
                }
                x0 = x1;
            }
        }

        @Override
        public double levelToExperience(int level) {
            return Math.ceil(180 * level * Math.sqrt(level) + 300);
        }

    }

    private enum Multiplier {

        PLAYER(HyriPlayerRankType.PLAYER, 1.0D),
        VIP(HyriPlayerRankType.VIP, 1.50D),
        VIP_PLUS(HyriPlayerRankType.VIP_PLUS, 1.75D),
        EPIC(HyriPlayerRankType.EPIC, 2.0D),
        HYRI_PLUS(HyriPlayerRankType.EPIC, 2.50D);

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
