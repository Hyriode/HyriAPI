package fr.hyriode.api.impl.common.player.packet;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 10:16
 */
public class PlayerTitlePacket extends TitlePacket {

    private final UUID playerId;

    public PlayerTitlePacket(UUID playerId, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super(title, subtitle, fadeIn, stay, fadeOut);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

}
