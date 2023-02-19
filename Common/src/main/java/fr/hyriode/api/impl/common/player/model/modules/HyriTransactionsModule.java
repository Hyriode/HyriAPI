package fr.hyriode.api.impl.common.player.model.modules;

import fr.hyriode.api.impl.common.player.model.HyriTransaction;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.player.model.IHyriTransaction;
import fr.hyriode.api.player.model.IHyriTransactionContent;
import fr.hyriode.api.player.model.modules.IHyriTransactionsModule;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * Created by AstFaster
 * on 17/02/2023 at 13:03.<br>
 *
 * Extends {@link HashMap} for better JSON serialization.
 */
public class HyriTransactionsModule extends HashMap<String, List<IHyriTransaction>> implements IHyriTransactionsModule, MongoSerializable, DataSerializable {

    @Override
    public void save(MongoDocument document) {
        for (Entry<String, List<IHyriTransaction>> entry : this.entrySet()) {
            final List<Document> serializedTransactions = new ArrayList<>();

            for (IHyriTransaction transaction : entry.getValue()) {
                serializedTransactions.add(MongoSerializer.serialize((HyriTransaction) transaction));
            }

            document.append(entry.getKey(), serializedTransactions);
        }
    }

    @Override
    public void load(MongoDocument document) {
        for (String key : document.keySet()) {
            final List<Document> transactionsDocuments = document.getList(key, Document.class);
            final List<IHyriTransaction> transactions = new ArrayList<>();

            for (Document transactionDocument : transactionsDocuments) {
                final HyriTransaction transaction = new HyriTransaction();

                transaction.load(MongoDocument.of(transactionDocument));
                transactions.add(transaction);
            }

            this.put(key, transactions);
        }
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeInt(this.size());

        for (Map.Entry<String, List<IHyriTransaction>> entry : this.entrySet()) {
            final List<IHyriTransaction> transactions = entry.getValue();

            output.writeString(entry.getKey());
            output.writeInt(transactions.size());

            for (IHyriTransaction transaction : transactions) {
                ((HyriTransaction) transaction).write(output);
            }
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            final String key = input.readString();
            final List<IHyriTransaction> transactions = new ArrayList<>();
            final int transactionsSize = input.readInt();

            for (int j = 0; j < transactionsSize; j++) {
                final HyriTransaction transaction = new HyriTransaction();

                transaction.read(input);
                transactions.add(transaction);
            }

            this.put(key, transactions);
        }
    }

    @Override
    public @NotNull Set<String> types() {
        return this.keySet();
    }

    @Override
    public List<IHyriTransaction> getAll(@NotNull String type) {
        return super.get(type);
    }

    @Override
    public IHyriTransaction get(@NotNull String type, @NotNull String name) {
        final List<IHyriTransaction> transactions = this.getAll(type);

        if (transactions == null) {
            return null;
        }

        for (IHyriTransaction transaction : transactions) {
            if (transaction.name().equals(name)) {
                return transaction;
            }
        }
        return null;
    }

    @Override
    public IHyriTransaction add(@NotNull String type, @NotNull String name, @NotNull IHyriTransactionContent content) {
        if (this.get(type, name) != null) {
            throw new IllegalStateException("A transaction already exists!");
        }

        final List<IHyriTransaction> transactions = super.getOrDefault(type, new ArrayList<>());
        final HyriTransaction transaction = new HyriTransaction(name, System.currentTimeMillis(), content);

        transactions.add(transaction);

        this.put(type, transactions);

        return transaction;
    }

    @Override
    public IHyriTransaction add(@NotNull String type, IHyriTransactionContent content) {
        return this.add(type, UUID.randomUUID().toString().split("-")[0], content);
    }

    @Override
    public void remove(@NotNull String type, @NotNull String name) {
        final List<IHyriTransaction> transactions = this.getAll(type);

        if (transactions == null) {
            return;
        }

        final IHyriTransaction transaction = this.get(type, name);

        transactions.remove(transaction);

        this.put(type, transactions);
    }

    @Override
    public boolean has(@NotNull String type, @NotNull String name) {
        return this.get(name, type) != null;
    }

}
