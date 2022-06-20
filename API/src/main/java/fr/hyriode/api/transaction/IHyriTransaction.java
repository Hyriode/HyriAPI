package fr.hyriode.api.transaction;

/**
 * Created by AstFaster
 * on 11/06/2022 at 19:57
 */
public interface IHyriTransaction {

    String name();

    long timestamp();

    <T extends IHyriTransactionContent> T content(Class<T> contentClass);

}
