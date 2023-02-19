package fr.hyriode.api.player.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by AstFaster
 * on 11/06/2022 at 19:57.<br>
 *
 * Represents a transaction made by a player at a given timestamp.
 */
public interface IHyriTransaction {

    /**
     * Get the name of the transaction
     *
     * @return A name
     */
    String name();

    /**
     * Get the timestamp of the transaction
     *
     * @return A timestamp (in milliseconds)
     */
    long timestamp();

    /**
     * Get the content of the transaction
     *
     * @return The {@linkplain IHyriTransactionContent content} of the transaction
     * @param <T> The type of the transaction to get
     */
    <T extends IHyriTransactionContent> T content();

    /**
     * Load the content of the transaction
     *
     * @param emptyContent The content to fill with the data
     * @return The loaded {@linkplain IHyriTransactionContent transaction content}
     * @param <T> The type of the content to return
     */
    <T extends IHyriTransactionContent> T loadContent(@NotNull T emptyContent);

}
