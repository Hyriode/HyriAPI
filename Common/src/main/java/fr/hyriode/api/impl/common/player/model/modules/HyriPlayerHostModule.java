package fr.hyriode.api.impl.common.player.model.modules;

import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.model.modules.IHyriPlayerHostModule;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 17/02/2023 at 15:22
 */
public class HyriPlayerHostModule implements IHyriPlayerHostModule, MongoSerializable, DataSerializable {

    private int availableHosts;
    private final Set<UUID> bannedPlayers = new HashSet<>();
    private final Set<String> favoriteConfigs = new HashSet<>();

    @Override
    public void save(MongoDocument document) {
        document.append("available", this.availableHosts);
        document.append("banned_players", this.bannedPlayers.stream().map(UUID::toString).collect(Collectors.toList()));
        document.append("favorite_configs", this.favoriteConfigs);
    }

    @Override
    public void load(MongoDocument document) {
        this.availableHosts = document.getInteger("available");
        document.getList("banned_players", String.class).forEach(uuid -> this.bannedPlayers.add(UUID.fromString(uuid)));
        this.favoriteConfigs.addAll(document.getList("favorite_configs", String.class));
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeInt(this.availableHosts);
        output.writeInt(this.bannedPlayers.size());

        for (UUID playerId : this.bannedPlayers) {
            output.writeUUID(playerId);
        }

        output.writeInt(this.favoriteConfigs.size());

        for (String config : this.favoriteConfigs) {
            output.writeString(config);
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.availableHosts = input.readInt();

        final int playersSize = input.readInt();

        for (int i = 0; i < playersSize; i++) {
            this.bannedPlayers.add(input.readUUID());
        }

        final int configsSize = input.readInt();

        for (int i = 0; i < configsSize; i++) {
            this.favoriteConfigs.add(input.readString());
        }
    }

    @Override
    public int getAvailableHosts() {
        return this.availableHosts;
    }

    @Override
    public void addAvailableHosts(int hosts) {
        this.availableHosts += hosts;
    }

    @Override
    public void setAvailableHosts(int hosts) {
        this.availableHosts = hosts;
    }

    @Override
    public @NotNull Set<UUID> getBannedPlayers() {
        return this.bannedPlayers;
    }

    @Override
    public void addBannedPlayer(@NotNull UUID playerId) {
        this.bannedPlayers.add(playerId);
    }

    @Override
    public void removeBannedPlayer(@NotNull UUID playerId) {
        this.bannedPlayers.remove(playerId);
    }

    @Override
    public boolean hasBannedPlayer(@NotNull UUID playerId) {
        return this.bannedPlayers.contains(playerId);
    }

    @Override
    public @NotNull Set<String> getFavoriteConfigs() {
        return this.favoriteConfigs;
    }

    @Override
    public void addFavoriteConfig(@NotNull String config) {
        this.favoriteConfigs.add(config);
    }

    @Override
    public void removeFavoriteConfig(@NotNull String config) {
        this.favoriteConfigs.remove(config);
    }

    @Override
    public boolean hasFavoriteConfig(@NotNull String config) {
        return this.favoriteConfigs.contains(config);
    }

}
