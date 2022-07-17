package fr.hyriode.api.language;

/**
 * Created by AstFaster
 * on 16/07/2022 at 23:48
 */
@FunctionalInterface
public interface IHyriLanguageAdapter<T> {

    /**
     * Get the value of a message from the object linked to this adapter
     *
     * @param message The message
     * @param object The object to use
     * @return The value
     */
    String getValue(HyriLanguageMessage message, T object);

}
