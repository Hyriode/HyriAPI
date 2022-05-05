package fr.hyriode.api.impl.common.leveling;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.impl.common.money.Hyris;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.leveling.event.HyriGainLevelEvent;
import fr.hyriode.api.leveling.event.HyriGainXPEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 12:42
 */
public class NetworkLeveling implements IHyriLeveling {

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
    public void addExperience(double experience) {
        this.runAction(() -> this.experience += experience);
    }

    @Override
    public void removeExperience(double experience) {
        this.runAction(() -> this.experience -= experience);
    }

    @Override
    public int getLevel() {
        return new Algo().experienceToLevel(this.experience);
    }

    @Override
    public Algorithm getAlgorithm() {
        return new Algo();
    }

    private void runAction(Runnable action) {
        final int oldLevel = this.getLevel();
        final double oldExperience = this.experience;

        action.run();

        final int newLevel = this.getLevel();
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(this.playerId);
        final IHyriEventBus eventBus = HyriAPI.get().getNetworkManager().getEventBus();

        if (oldExperience != this.experience) {
            eventBus.publish(new HyriGainXPEvent(account.getUniqueId(), this.name, oldExperience, this.experience));
        }

        if (newLevel > oldLevel) {
            eventBus.publish(new HyriGainLevelEvent(account.getUniqueId(), this.name, oldLevel, newLevel));
        }
    }

    @Override
    public long multiply(long currentExperience, IHyriPlayer account) {
        final Multiplier multiplier = Multiplier.getByPlayer(account);

        if (multiplier != null) {
            return (long) (currentExperience * multiplier.getAmount());
        }
        return currentExperience;
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

    public static class Algo implements Algorithm {

        private static final double CONSTANT = 0.1D;

        @Override
        public int experienceToLevel(double experience) {
            return (int) Math.floor(CONSTANT * Math.sqrt(experience));
        }

        @Override
        public double levelToExperience(int level) {
            return Math.pow(level / CONSTANT, 2);
        }

    }

}
