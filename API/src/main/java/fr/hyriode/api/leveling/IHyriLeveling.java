package fr.hyriode.api.leveling;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 12:34
 */
public interface IHyriLeveling {

    /**
     * Get the name of the leveling system
     *
     * @return A name
     */
    String getName();

    /**
     * Get the amount of experience
     *
     * @return The experience
     */
    double getExperience();

    /**
     * Set the amount of experience
     *
     * @param experience New experience amount
     */
    void setExperience(double experience);

    /**
     * Add a given amount of experience
     *
     * @param experience The amount to add
     * @param multipliers Are multipliers used
     * @return The added experience (with multipliers)
     */
    double addExperience(double experience, boolean multipliers);

    /**
     * Add a given amount of experience
     *
     * @param experience The amount to add
     * @return The added experience (with multipliers)
     */
    default double addExperience(double experience) {
        return this.addExperience(experience, true);
    }

    /**
     * Remove a given amount of experience
     *
     * @param experience The amount to remove
     */
    void removeExperience(double experience);

    /**
     * Get the experience as a level
     *
     * @return The level based on the experience
     */
    int getLevel();

    /**
     * Check if the current level is above another one
     *
     * @param level The level to check
     * @return <code>true</code> if the current level is above
     */
    default boolean hasLevel(int level) {
        return this.getLevel() >= level;
    }

    /**
     * Get the algorithm of the leveling system used to do calculations, etc.
     *
     * @return A {@link Algorithm} instance
     */
    Algorithm getAlgorithm();

    /**
     * Multiply an amount of experience for a given player
     *
     * @param experience The experience to multiply
     * @return The new multiplied experience
     */
    default double applyMultiplier(double experience) {
        return experience;
    }

    /**
     * The interface that represents a leveling algorithm
     */
    interface Algorithm {

        /**
         * Transform the experience to level
         *
         * @param experience The amount of experience to transform
         * @return A level
         */
        int experienceToLevel(double experience);

        /**
         * Transform a level to an amount of experience
         *
         * @param level The level to transform
         * @return An amount of experience
         */
        double levelToExperience(int level);

    }

}
