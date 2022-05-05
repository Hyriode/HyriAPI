package fr.hyriode.api.chat.packet;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 03/05/2022 at 19:18
 */
public class BroadcastPacket extends ComponentPacket {

    private final UUID sender;

    public BroadcastPacket(String component, UUID sender) {
        super(component);
        this.sender = sender;
    }

    public UUID getSender() {
        return this.sender;
    }

}
