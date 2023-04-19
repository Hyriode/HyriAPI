package fr.hyriode.api.booster;

import fr.hyriode.api.player.IHyriPlayer;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
     * @param owner The owner of the booster
     * @param game The game that will be boosted
     * @param multiplier The multiplier of the booster
     * @param duration The duration of the booster (in seconds)
     * @return The created {@link IHyriBooster}
     */
    IHyriBooster enableBooster(UUID owner, String game, double multiplier, long duration);

    /**
     * Give a booster to a given player
     *
     * @param player The player account
     * @param multiplier The multiplier of the booster
     * @param duration The duration of the booster (in seconds)
     */
    void giveBooster(IHyriPlayer player, double multiplier, long duration);

    /**
     * Get the owned boosters of a player
     *
     * @param player The player
     * @return A map of {@link HyriBoosterTransaction} linked to their name
     */
    Map<String, HyriBoosterTransaction> getPlayerBoosters(IHyriPlayer player);

    /**
     * Get all boosters on the network
     *
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getBoosters();

    /**
     * Get all boosters on a given game
     *
     * @param game A game name (E.g bedwars, therunner)
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getBoosters(String game);

    /**
     * Get all actives boosters on the network
     *
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getActiveBoosters();

    /**
     * Get all actives boosters on a given game
     *
     * @param game A game name (E.g bedwars, therunner)
     * @return A list of {@link IHyriBooster}
     */
    List<IHyriBooster> getActiveBoosters(String game);

    /**
     * Get an active booster by its identifier
     *
     * @param identifier The identifier of the booster
     * @return The {@link IHyriBooster} linked to the identifier
     */
    IHyriBooster getBooster(UUID identifier);

    /**
     * Get all the players that thanked a given booster
     *
     * @param booster The {@link UUID} of the booster
     * @return A list of player {@link UUID}
     */
    Set<UUID> getThanks(UUID booster);

    /**
     * Add a player to those who thanked a booster
     *
     * @param booster The {@link UUID} of the booster
     * @param player The {@link UUID} of the player
     */
    void addThank(UUID booster, UUID player);

    /**
     * Check whether a player has thanked a given booster
     *
     * @param booster The {@link UUID} of the booster
     * @param player The {@link UUID} of the player
     * @return <code>true</code> if yes
     */
    boolean hasThanked(UUID booster, UUID player);

}
