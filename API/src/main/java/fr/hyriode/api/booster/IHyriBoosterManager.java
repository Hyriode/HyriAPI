package fr.hyriode.api.booster;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:26
 */
public interface IHyriBoosterManager {

    /** The booster will be applied multiplier over all games */
    String GLOBAL_TYPE = "global";
    /** The booster type will be selected at the activation */
    String SELECTABLE_TYPE = "selectable";

    /**
     * Enable a booster on the network
     *
     * @param owner The owner of the booster
     * @param type The booster type
     * @param multiplier The multiplier of the booster
     * @param duration The duration of the booster (in seconds)
     * @return The created {@link IHyriBooster}
     */
    IHyriBooster enableBooster(UUID owner, String type, double multiplier, long duration);

    /**
     * Give a booster to a given player
     *
     * @param player The player account
     * @param type The type of the booster to give
     * @param multiplier The multiplier of the booster
     * @param duration The duration of the booster (in seconds)
     */
    void giveBooster(IHyriPlayer player, String type, double multiplier, long duration);

    /**
     * Get the owned boosters of a player
     *
     * @param player The player
     * @return A list of {@link HyriBoosterTransaction}
     */
    List<HyriBoosterTransaction> getPlayerBoosters(IHyriPlayer player);

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
