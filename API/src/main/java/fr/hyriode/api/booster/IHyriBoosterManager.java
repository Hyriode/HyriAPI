package fr.hyriode.api.booster;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:26
 */
public interface IHyriBoosterManager {

    String GLOBAL_TYPE = "global";

    /**
     * Enable a booster on the network
     *
     * @param type The booster type
     * @param multiplier The multiplier of the booster
     * @param duration The duration of the booster (in seconds)
     * @return The created {@link IHyriBooster}
     */
    IHyriBooster enableBooster(String type, double multiplier, UUID owner, long duration);

    /**
     * Get all actives boosters on the network
     *
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getBoosters();

    /**
     * Get all actives boosters with a given type on the network
     *
     * @param type A booster type (ex: {@link IHyriBoosterManager#GLOBAL_TYPE}, bedwars, therunner, etc)
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getBoosters(String type);

    /**
     * Get an active booster by its identifier
     *
     * @param identifier The identifier of the booster
     * @return The {@link IHyriBooster} linked to the identifier
     */
    IHyriBooster getBooster(UUID identifier);

}
