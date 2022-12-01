package fr.hyriode.api.impl.common.player.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/05/2022 at 18:18
 */
public class PlayerKickPacket extends HyriPacket {

    private final UUID playerId;
    private final String component;

    public PlayerKickPacket(UUID playerId, String component) {
        this.playerId = playerId;
        this.component = component;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public String getComponent() {
        return this.component;
    }

}
