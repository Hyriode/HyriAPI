package fr.hyriode.api.game;

/**
 * Created by AstFaster
 * on 19/02/2023 at 09:32
 */
public interface IHyriGameType {

    /**
     * Get the identifier of the type
     *
     * @return An identifier
     */
    int getId();

    /**
     * Get the name of the type (e.g. SOLO)
     *
     * @return A name
     */
    String getName();

    /**
     * Get the display name of the type (e.g. Solo)
     *
     * @return A display name
     */
    String getDisplayName();

}
