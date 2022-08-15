package fr.hyriode.api.game;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:52
 */
public class HyriGameType {

    /** The id of the type used for priorities */
    private final int id;
    /** The display name of the type. Example: Solo, Doubles, etc. */
    private final String displayName;

    /**
     * Default constructor of a {@linkplain HyriGameType game type}
     *
     * @param id The id of the type
     * @param displayName The display name of the type
     */
    public HyriGameType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /**
     * Get the identifier of the type
     *
     * @return An identifier
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the display name of the type
     *
     * @return A display name
     */
    public String getDisplayName() {
        return this.displayName;
    }

}
