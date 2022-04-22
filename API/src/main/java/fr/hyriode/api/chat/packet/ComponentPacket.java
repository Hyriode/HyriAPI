package fr.hyriode.api.chat.packet;

import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 10:06
 */
public class ComponentPacket extends HyriPacket {

    protected final String component;

    public ComponentPacket(String component) {
        this.component = component;
    }

    public String getComponent() {
        return this.component;
    }

}
