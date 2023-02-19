package fr.hyriode.api.impl.common.player;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.impl.common.player.model.HyriNickname;
import fr.hyriode.api.impl.common.player.model.HyriRank;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import fr.hyriode.api.util.DataDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 20:49
 */
public class HyriPlayerSession implements IHyriPlayerSession, DataSerializable {

    private UUID playerId;
    private long loginDate;

    private UUID privateMessageTarget;
    private UUID party;
    private UUID queue;

    private String proxy;
    private String server;
    private String lastServer;

    private final HyriNickname nickname = new HyriNickname();

    private boolean playing;
    private boolean moderating;
    private boolean vanished;

    private final DataDictionary data = new DataDictionary();

    public HyriPlayerSession() {}

    public HyriPlayerSession(UUID playerId, long loginDate) {
        this.playerId = playerId;
        this.loginDate = loginDate;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.playerId);
        output.writeLong(this.loginDate);
        output.writeUUID(this.privateMessageTarget);
        output.writeUUID(this.party);
        output.writeUUID(this.queue);
        output.writeString(this.proxy);
        output.writeString(this.server);
        output.writeString(this.lastServer);

        this.nickname.write(output);

        output.writeBoolean(this.playing);
        output.writeBoolean(this.moderating);
        output.writeBoolean(this.vanished);
        output.writeInt(this.data.size());

        for (Map.Entry<String, String> entry : this.data.entrySet()) {
            output.writeString(entry.getKey());
            output.writeString(entry.getValue());
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.playerId = input.readUUID();
        this.loginDate = input.readLong();
        this.privateMessageTarget = input.readUUID();
        this.party = input.readUUID();
        this.queue = input.readUUID();
        this.proxy = input.readString();
        this.server = input.readString();
        this.lastServer = input.readString();

        this.nickname.read(input);

        this.playing = input.readBoolean();
        this.moderating = input.readBoolean();
        this.vanished = input.readBoolean();

        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            this.data.put(input.readString(), input.readString());
        }
    }

    @Override
    public UUID getPlayerId() {
        return this.playerId;
    }

    @Override
    public String getNameWithRank() {
        if (this.nickname.has()) {
            final PlayerRank rank = this.nickname.getRank();

            return HyriChatColor.translateAlternateColorCodes('&', rank.getDefaultPrefix() + (rank.withSeparator() ? HyriRank.SEPARATOR : "")  + rank.getDefaultColor().toString() + this.nickname.getName());
        }
        return IHyriPlayer.get(this.playerId).getNameWithRank();
    }

    @Override
    public long getLoginDate() {
        return this.loginDate;
    }

    @Override
    public @Nullable UUID getPrivateMessageTarget() {
        return this.privateMessageTarget;
    }

    @Override
    public void setPrivateMessageTarget(@NotNull UUID playerId) {
        this.privateMessageTarget = playerId;
    }

    @Override
    public @Nullable UUID getParty() {
        return this.party;
    }

    @Override
    public void setParty(UUID partyId) {
        this.party = partyId;
    }

    @Override
    public @Nullable UUID getQueue() {
        return this.queue;
    }

    @Override
    public void setQueue(UUID queueId) {
        this.queue = queueId;
    }

    @Override
    public String getProxy() {
        return this.proxy;
    }

    @Override
    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String getLastServer() {
        return this.lastServer;
    }

    @Override
    public void setLastServer(String lastServer) {
        this.lastServer = lastServer;
    }

    @Override
    public HyriNickname getNickname() {
        return this.nickname;
    }

    @Override
    public boolean isPlaying() {
        return this.playing;
    }

    @Override
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public boolean isModerating() {
        return this.moderating;
    }

    @Override
    public void setModerating(boolean moderating) {
        this.moderating = moderating;
    }

    @Override
    public boolean isVanished() {
        return this.vanished;
    }

    @Override
    public void setVanished(boolean vanished) {
        this.vanished = vanished;
    }

    @Override
    public DataDictionary getData() {
        return this.data;
    }

}
