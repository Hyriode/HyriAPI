package fr.hyriode.api.impl.common.player.model.modules;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.player.model.HyriFriend;
import fr.hyriode.api.impl.common.player.model.HyriFriendRequestImpl;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.HyriFriendRequest;
import fr.hyriode.api.player.model.IHyriFriend;
import fr.hyriode.api.player.model.modules.IHyriFriendsModule;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 18/02/2023 at 09:57.<br>
 *
 * Extends of {@link ArrayList} for a better JSON serialization.
 */
public class HyriFriendsModule extends ArrayList<IHyriFriend> implements IHyriFriendsModule, MongoSerializable, DataSerializable {

    private static final String REQUESTS_KEY = "friends-requests:";
    private static final int REQUESTS_TTL = 60 * 5;

    private final IHyriPlayer player;

    public HyriFriendsModule(IHyriPlayer player) {
        this.player = player;
    }

    @Override
    public void save(MongoDocument document) {
        final List<MongoDocument> documents = new ArrayList<>();

        for (IHyriFriend friend : this) {
            documents.add(MongoSerializer.serialize((HyriFriend) friend));
        }

        document.append("friends", documents);
    }

    @Override
    public void load(MongoDocument document) {
        final List<Document> documents = document.getList("friends", Document.class);

        for (Document friendDocument : documents) {
            final HyriFriend friend = new HyriFriend();

            friend.load(MongoDocument.of(friendDocument));

            this.add(friend);
        }
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeInt(this.size());

        for (IHyriFriend friend : this) {
            ((HyriFriend) friend).write(output);
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            final HyriFriend friend = new HyriFriend();

            friend.read(input);

            this.add(friend);
        }
    }

    @Override
    public void sendRequest(@NotNull UUID target) {
        final HyriFriendRequestImpl request = new HyriFriendRequestImpl();

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final byte[] key = (REQUESTS_KEY + target + ":" + this.player.getUniqueId().toString()).getBytes(StandardCharsets.UTF_8);

            pipeline.set(key, HyriAPI.get().getDataSerializer().serialize(request));
            pipeline.expire(key, REQUESTS_TTL);
            pipeline.sync();
        });
        HyriAPI.get().getPubSub().send(HyriChannel.FRIENDS, new HyriFriendRequestImpl(this.player.getUniqueId(), target));
    }

    @Override
    public void removeRequest(@NotNull UUID sender) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(REQUESTS_KEY + this.player.getUniqueId().toString() + ":" + sender));
    }

    @Override
    public boolean hasRequest(@NotNull UUID sender) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(REQUESTS_KEY + this.player.getUniqueId().toString() + ":" + sender));
    }

    @Override
    public @NotNull List<HyriFriendRequest> getRequests() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Set<String> keys = jedis.keys(REQUESTS_KEY + this.player.getUniqueId().toString() + ":*");
            final List<Response<byte[]>> responses = new ArrayList<>();

            final Pipeline pipeline = jedis.pipelined();

            for (String key : keys) {
                responses.add(pipeline.get(key.getBytes(StandardCharsets.UTF_8)));
            }

            pipeline.sync();

            return responses.stream().map(response -> HyriAPI.get().getDataSerializer().deserialize(new HyriFriendRequestImpl(), response.get())).collect(Collectors.toList());
        });
    }

    @Override
    public IHyriFriend get(UUID uuid) {
        for (IHyriFriend friend : this) {
            if (friend.getUniqueId().equals(uuid)) {
                return friend;
            }
        }
        return null;
    }

    @Override
    public @NotNull List<IHyriFriend> getAll() {
        return this;
    }

    @Override
    public void add(@NotNull UUID uuid) {
        if (this.has(uuid)) {
            return;
        }

        this.add(new HyriFriend(uuid, System.currentTimeMillis()));
    }

    @Override
    public void remove(@NotNull UUID uuid) {
        final IHyriFriend friend = this.get(uuid);

        if (friend != null) {
            this.remove(friend);
        }
    }

    @Override
    public boolean has(@NotNull UUID uuid) {
        for (IHyriFriend friend : this) {
            if (friend.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

}
