package fr.hyriode.api.language;

/**
 * Created by AstFaster
 * on 16/07/2022 at 23:48
 */
@FunctionalInterface
public interface IHyriLanguageAdapter<T> {

    String getValue(HyriLanguageMessage message, T object);

}
