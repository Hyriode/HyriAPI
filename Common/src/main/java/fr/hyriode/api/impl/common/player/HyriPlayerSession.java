package fr.hyriode.api.impl.common.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.impl.common.player.nickname.HyriNickname;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.nickname.HyriNicknameUpdatedEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.util.DataDictionary;
import fr.hyriode.api.util.Skin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 20:49
 */
public class HyriPlayerSession implements IHyriPlayerSession {

    private transient Date loginDateObj;

    private final UUID playerId;
    private final long loginDate;

    private UUID privateMessageTarget;
    private UUID party;
    private UUID queue;

    private String proxy;
    private String server;
    private String lastServer;

    private HyriNickname nickname;

    private boolean playing;
    private boolean moderating;
    private boolean vanished;

    private final DataDictionary data = new DataDictionary();

    public HyriPlayerSession(UUID playerId, long loginDate) {
        this.playerId = playerId;
        this.loginDate = loginDate;
    }

    @Override
    public UUID getPlayerId() {
        return this.playerId;
    }

    @Override
    public String getNameWithRank() {
        return this.nickname != null ? HyriChatColor.translateAlternateColorCodes('&', this.nickname.getRank().getDefaultPrefix() + HyriRank.SEPARATOR + this.nickname.getRank().getDefaultColor().toString() + this.nickname.getName()) : IHyriPlayer.get(this.playerId).getNameWithRank();
    }

    @Override
    public Date getLoginDate() {
        return this.loginDateObj != null ? this.loginDateObj : (this.loginDateObj = new Date(this.loginDate));
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
    public @Nullable IHyriNickname getNickname() {
        return this.nickname;
    }

    @Override
    public @NotNull IHyriNickname createNickname(String name, String skinOwner, Skin skin) {
        return this.nickname = new HyriNickname(name, skinOwner, skin);
    }

    @Override
    public void setNickname(IHyriNickname nickname) {
        HyriAPI.get().getEventBus().publishAsync(new HyriNicknameUpdatedEvent(this.playerId, nickname));

        this.nickname = (HyriNickname) nickname;
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
