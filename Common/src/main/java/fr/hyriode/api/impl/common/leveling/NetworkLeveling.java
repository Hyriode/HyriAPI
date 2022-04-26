package fr.hyriode.api.impl.common.leveling;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.leveling.event.HyriGainLevelEvent;
import fr.hyriode.api.player.IHyriPlayer;

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

        action.run();

        final int newLevel = this.getLevel();

        if (newLevel > oldLevel) {
            final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(this.playerId);

            HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new HyriGainLevelEvent(account.getUniqueId(), this.name, oldLevel, newLevel));
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
