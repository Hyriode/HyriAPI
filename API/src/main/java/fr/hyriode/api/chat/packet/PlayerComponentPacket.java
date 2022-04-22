package fr.hyriode.api.chat.packet;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 08:44
 */
public class PlayerComponentPacket extends ComponentPacket {

    private final UUID player;

    public PlayerComponentPacket(UUID player, String component) {
        super(component);
        this.player = player;
    }

    public UUID getPlayer() {
        return this.player;
    }

}
