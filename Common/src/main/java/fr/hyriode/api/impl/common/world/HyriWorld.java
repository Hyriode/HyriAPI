package fr.hyriode.api.impl.common.world;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.world.IHyriWorld;
import fr.hyriode.api.world.IHyriWorldManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 19/02/2023 at 18:54
 */
public class HyriWorld implements IHyriWorld, MongoSerializable {

    private String initialName;

    private final String database;
    private String category;
    private String name;

    private boolean enabled;
    private long creationDate;

    private List<String> authors;

    public HyriWorld(String database, String name) {
        this.database = database;
        this.name = name;
        this.initialName = this.name;
    }

    public HyriWorld(String database, String category, String name, List<String> authors) {
        this.database = database;
        this.category = category;
        this.name = name;
        this.enabled = true;
        this.creationDate = System.currentTimeMillis();
        this.authors = authors;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("category", this.category);
        document.append("enabled", this.enabled);
        document.append("creationDate", this.creationDate);
        document.append("authors", this.authors);
    }

    @Override
    public void load(MongoDocument document) {
        this.category = document.getString("category");
        this.enabled = document.getBoolean("enabled");
        this.creationDate = document.getLong("creationDate");
        this.authors = document.getList("authors", String.class);
    }

    @Override
    public @NotNull String getDatabase() {
        return this.database;
    }

    @Override
    public @NotNull String getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(@NotNull String category) {
        this.category = category;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public String getInitialName() {
        return this.initialName;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public long getCreationDate() {
        return this.creationDate;
    }

    @Override
    public @NotNull List<String> getAuthors() {
        return this.authors;
    }

    @Override
    public void addAuthor(@NotNull String author) {
        this.authors.add(author);
    }

    @Override
    public void removeAuthor(@NotNull String author) {
        this.authors.remove(author);
    }

    @Override
    public void save(@NotNull File worldFolder) {
        HyriAPI.get().getWorldManager().saveWorld(this, worldFolder);
    }

    @Override
    public void save(@NotNull UUID worldId) {
        HyriAPI.get().getWorldManager().saveWorld(this, worldId);
    }

    @Override
    public void load(@NotNull File destinationFolder) {
        HyriAPI.get().getWorldManager().loadWorld(this, destinationFolder);
    }

    @Override
    public void delete() {
        HyriAPI.get().getWorldManager().deleteWorld(this);
    }

    @Override
    public void update() {
        HyriAPI.get().getWorldManager().updateWorld(this);
    }

    public static class Builder implements IBuilder {

        private String database;
        private String category = IHyriWorldManager.DEFAULT_CATEGORY;
        private String name;

        private List<String> authors = new ArrayList<>();

        @Override
        public @NotNull IBuilder withDatabase(@NotNull String database) {
            this.database = database;
            return this;
        }

        @Override
        public @NotNull IBuilder withCategory(@NotNull String category) {
            this.category = category;
            return this;
        }

        @Override
        public @NotNull IBuilder withName(@NotNull String name) {
            this.name = name;
            return this;
        }

        @Override
        public @NotNull IBuilder withAuthors(@NotNull List<String> authors) {
            this.authors = authors;
            return this;
        }

        @Override
        public @NotNull IBuilder addAuthors(@NotNull String... authors) {
            Collections.addAll(this.authors, authors);
            return this;
        }

        @Override
        public @NotNull IHyriWorld build() {
            return new HyriWorld(this.database, this.category, this.name, this.authors);
        }

    }

}
