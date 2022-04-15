package fr.hyriode.api.booster;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:26
 */
public interface IHyriBoosterManager {

    /**
     * Enable a booster on the network
     *
     * @param type The booster type
     * @param multiplier The multiplier of the booster
     * @param purchaser The uuid of the player who purchased the booster
     * @param purchaseDate The date when the booster was purchased
     * @param duration The duration of the booster (in seconds)
     * @return The created {@link IHyriBooster}
     */
    IHyriBooster enableBooster(String type, double multiplier, UUID purchaser, long purchaseDate, int duration);

    /**
     * Get all actives boosters on the network
     *
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getActiveBoosters();

    /**
     * Get all actives boosters with a given type on the network
     *
     * @param type A booster type (ex: ALL, BEDWARS, THE_RUNNER, etc)
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getActiveBoosters(String type);

    /**
     * Get an active booster by its identifier
     *
     * @param identifier The identifier of the booster
     * @return The {@link IHyriBooster} linked to the identifier
     */
    IHyriBooster getBooster(UUID identifier);

    /**
     * Add an active booster on the network.<br>
     * Warning: the booster needs to be active before adding it!
     *
     * @param booster The booster to add on the network
     */
    void addBooster(IHyriBooster booster);

    /**
     * Remove a booster from the network by giving its identifier
     *
     * @param identifier The identifier of the booster
     */
    void removeBooster(UUID identifier);

    /**
     * Remove a booster from the network
     *
     * @param booster The booster to remove
     */
    default void removeBooster(IHyriBooster booster) {
        this.removeBooster(booster.getIdentifier());
    }

}
