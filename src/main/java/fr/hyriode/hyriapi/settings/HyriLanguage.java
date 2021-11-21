package fr.hyriode.hyriapi.settings;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 10:33
 */
public enum HyriLanguage {

    /** English language */
    EN("en"),

    /** French language */
    FR("fr");

    /** HyriLanguage code */
    private final String code;

    /**
     * Constructor of {@link HyriLanguage}
     *
     * @param code - Language code
     */
    HyriLanguage(String code) {
        this.code = code;
    }

    /**
     * Get the code of the language
     *
     * @return - Language code
     */
    public String getCode() {
        return this.code;
    }

}
