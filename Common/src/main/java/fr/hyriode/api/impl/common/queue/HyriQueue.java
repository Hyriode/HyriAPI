package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 30/06/2022 at 10:46
 */
public class HyriQueue implements IHyriQueue, DataSerializable {

    private UUID id;
    private Type type;

    private String game;
    private String gameType;
    private String map;
    private String server;

    private final Set<UUID> players = new HashSet<>();

    public HyriQueue() {}

    public HyriQueue(Type type, String game, String gameType, String map, String server) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.game = game;
        this.gameType = gameType;
        this.map = map;
        this.server = server;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.id);
        output.writeString(this.type.name());
        output.writeString(this.game);
        output.writeString(this.gameType);
        output.writeString(this.map);
        output.writeString(this.server);
        output.writeInt(this.players.size());

        for (UUID player : this.players) {
            output.writeUUID(player);
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.id = input.readUUID();
        this.type = Type.valueOf(input.readString());
        this.game = input.readString();
        this.gameType = input.readString();
        this.map = input.readString();
        this.server = input.readString();

        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            this.players.add(input.readUUID());
        }
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public @NotNull Type getType() {
        return this.type;
    }

    @Override
    public @NotNull String getGame() {
        return this.game;
    }

    @Override
    public @NotNull String getGameType() {
        return this.gameType;
    }

    @Override
    public @Nullable String getMap() {
        return this.map;
    }

    @Override
    public @Nullable String getServer() {
        return this.server;
    }

    @Override
    public @NotNull Set<UUID> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    public void addPlayer(UUID playerId) {
        this.players.add(playerId);
    }

    public void removePlayer(UUID playerId) {
        this.players.remove(playerId);
    }

    @Override
    public @NotNull Set<Set<UUID>> getTotalPlayers() {
        final Set<Set<UUID>> totalPlayers = new HashSet<>();

        for (UUID player : this.players) {
            final Set<UUID> group = new HashSet<>();
            final IHyriParty party = HyriAPI.get().getPartyManager().getPlayerParty(player);

            for (UUID member : party.getMembers().keySet()) {
                final IHyriPlayerSession session = IHyriPlayerSession.get(member);

                if (session == null || session.isPlaying() || session.isModerating()) {
                    continue;
                }

                group.add(member);
            }

            totalPlayers.add(group);
        }
        return totalPlayers;
    }

}
