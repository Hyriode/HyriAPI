package fr.hyriode.api.impl.common.player.model.modules;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.modules.IHyriPlayerHostModule;
import fr.hyriode.api.rank.PlayerRank;
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

    @Expose
    private int availableHosts;
    @Expose
    private final Set<UUID> bannedPlayers = new HashSet<>();
    @Expose
    private final Set<String> favoriteConfigs = new HashSet<>();

    private final IHyriPlayer account;

    public HyriPlayerHostModule(IHyriPlayer account) {
        this.account = account;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("available", this.availableHosts);
        document.append("bannedPlayers", this.bannedPlayers.stream().map(UUID::toString).collect(Collectors.toList()));
        document.append("favoriteConfigs", this.favoriteConfigs);
    }

    @Override
    public void load(MongoDocument document) {
        this.availableHosts = document.getInteger("available");
        document.getList("bannedPlayers", String.class).forEach(uuid -> this.bannedPlayers.add(UUID.fromString(uuid)));
        this.favoriteConfigs.addAll(document.getList("favoriteConfigs", String.class));
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
        if (this.account.getRank().isSuperior(PlayerRank.PARTNER)) {
            return 1;
        }

        if (this.account.getHyriPlus().has()) {
            return 1;
        }

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
