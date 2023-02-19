package fr.hyriode.api.guild;

/**
 * Created by AstFaster
 * on 13/02/2023 at 22:06.<br>
 *
 * This class represents the resources of a {@linkplain IHyriGuild guild}: Hyris.
 */
public interface IGuildChest {

    /**
     * Get the total amount of Hyris stored in the chest
     *
     * @return A Hyris amount
     */
    long getHyris();

    /**
     * Add an amount of Hyris into the chest
     *
     * @param hyris A Hyris amount to add
     */
    void addHyris(long hyris);

    /**
     * Remove an amount of Hyris from the chest
     *
     * @param hyris A Hyris amount to remove
     */
    void removeHyris(long hyris);

}
