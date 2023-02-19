package fr.hyriode.api.player.model.modules;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriTransaction;
import fr.hyriode.api.player.model.IHyriTransactionContent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Created by AstFaster
 * on 17/02/2023 at 11:48.<br>
 *
 * The module used to manage the {@linkplain IHyriTransaction transactions} made by a {@linkplain IHyriPlayer player}.
 */
public interface IHyriTransactionsModule {

    /**
     * Get all the types of transactions stored
     *
     * @return A list of type (e.g. boosters, ranks)
     */
    @NotNull Set<String> types();

    /**
     * Get all the {@linkplain IHyriTransaction transactions} for a given type
     *
     * @param type The type of the transactions to get
     * @return The list of {@linkplain IHyriTransaction transactions} linked to the type (if it exists)
     */
    List<IHyriTransaction> getAll(@NotNull String type);

    /**
     * Get a transaction by its type and name
     *
     * @param type The type of the transaction to get
     * @param name The name of the transaction to get
     * @return The found {@linkplain IHyriTransaction transaction}; or <code>null</code> if it doesn't exist
     */
    IHyriTransaction get(@NotNull String type, @NotNull String name);

    /**
     * Add a new transaction to the player's account
     *
     * @param type The type of the transaction to add
     * @param name The name of the transaction to add
     * @param content The content of the transaction to add
     * @return The created {@linkplain IHyriTransaction transaction}
     */
    IHyriTransaction add(@NotNull String type, @NotNull String name, @NotNull IHyriTransactionContent content);

    /**
     * Add a new transaction to the player's account.<br>
     * With this method, the name of the transaction will be auto-generated
     *
     * @param type The type of the transaction to add
     * @param content The content of the transaction to add
     * @return The created {@linkplain IHyriTransaction transaction}
     */
    IHyriTransaction add(@NotNull String type, IHyriTransactionContent content);

    /**
     * Remove a transaction by giving its type and name
     *
     * @param type The type of the transaction to remove
     * @param name The name of the transaction to remove
     */
    void remove(@NotNull String type, @NotNull String name);

    /**
     * Check whether a transaction exists for a given type and name
     *
     * @param type The type of the transaction to find
     * @param name The name of the transaction to find
     * @return <code>true</code> if the transactions exists
     */
    boolean has(@NotNull String type, @NotNull String name);

}
