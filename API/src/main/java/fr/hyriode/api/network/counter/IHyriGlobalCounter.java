package fr.hyriode.api.network.counter;

/**
 * Created by AstFaster
 * on 08/07/2022 at 10:31
 */
public interface IHyriGlobalCounter {

    /**
     * Get the number of players that are on the server
     *
     * @return An amount of players
     */
    int getPlayers();

    /**
     * Get the counter of a category
     *
     * @param name The name of the category to get
     * @return The {@linkplain  IHyriCategoryCounter category counter} object
     */
    IHyriCategoryCounter getCategory(String name);

}
