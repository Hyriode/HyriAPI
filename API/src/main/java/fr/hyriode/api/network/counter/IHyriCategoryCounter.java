package fr.hyriode.api.network.counter;

/**
 * Created by AstFaster
 * on 08/07/2022 at 10:34
 */
public interface IHyriCategoryCounter {

    /**
     * Get the amount of players in the category
     *
     * @return An amount of players
     */
    int getPlayers();

    /**
     * Get the amount of players in a subtype.<br>
     * Example: the category is bedwars and the subtype is SOLO
     *
     * @param type The name of the subtype
     * @return An amount of players
     */
    int getPlayers(String type);

}
