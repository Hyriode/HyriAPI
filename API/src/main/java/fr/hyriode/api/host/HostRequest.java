package fr.hyriode.api.host;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 03/12/2022 at 15:34.<br>
 *
 * The request to send to create a new host.
 */
public class HostRequest extends HyriPacket {

    /** The type of the host to create */
    private HostType hostType = HostType.PUBLIC;
    /** The {@link UUID} of the owner of the host */
    private UUID owner;
    /** The game running on the host */
    private String game;
    /** The type of the game running on the host */
    private String gameType;

    public HostRequest(HostType hostType, UUID owner, String game, String gameType) {
        this.hostType = hostType;
        this.owner = owner;
        this.game = game;
        this.gameType = gameType;
    }

    public HostRequest() {}

    public HostType getHostType() {
        return this.hostType;
    }

    public HostRequest withHostType(HostType hostType) {
        this.hostType = hostType;
        return this;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public HostRequest withOwner(UUID owner) {
        this.owner = owner;
        return this;
    }

    public String getGame() {
        return this.game;
    }

    public HostRequest withGame(String game) {
        this.game = game;
        return this;
    }

    public String getGameType() {
        return this.gameType;
    }

    public HostRequest withGameType(String gameType) {
        this.gameType = gameType;
        return this;
    }

    /**
     * Send the request
     */
    public void send() {
        HyriAPI.get().getPubSub().send(IHostManager.CHANNEL, this);
    }

}
