package fr.hyriode.api.settings;

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

    /**
     * Get the language by giving its code
     *
     * @param code A language code
     * @return A {@link HyriLanguage}
     */
    public static HyriLanguage getByCode(String code) {
        for (HyriLanguage language : values()) {
            if (language.getCode().equalsIgnoreCase(code)) {
                return language;
            }
        }
        return null;
    }

}
