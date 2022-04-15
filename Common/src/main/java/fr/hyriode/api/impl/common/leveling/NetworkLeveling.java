package fr.hyriode.api.impl.common.leveling;

import fr.hyriode.api.leveling.IHyriLeveling;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 12:42
 */
public class NetworkLeveling implements IHyriLeveling {

    private final String name;
    private double experience;

    private final transient IHyriLeveling.Algorithm algorithm = new Algo();

    public NetworkLeveling() {
        this.name = "network";
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
        this.experience = experience;
    }

    @Override
    public void addExperience(double experience) {
        this.experience += experience;
    }

    @Override
    public void removeExperience(double experience) {
        this.experience -= experience;
    }

    @Override
    public int getLevel() {
        return this.algorithm.experienceToLevel(this.experience);
    }

    @Override
    public Algorithm getAlgorithm() {
        return this.algorithm;
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
