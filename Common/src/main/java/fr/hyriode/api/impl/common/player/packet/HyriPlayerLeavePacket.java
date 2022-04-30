package fr.hyriode.api.impl.common.player.packet;

import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 30/04/2022 at 11:24
 */
public class HyriPlayerLeavePacket extends HyriPacket {

    private final UUID player;

    public HyriPlayerLeavePacket(UUID player) {
        this.player = player;
    }

    public UUID getPlayer() {
        return this.player;
    }

}
