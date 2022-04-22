package fr.hyriode.api.server.join.packet;

import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:59
 */
public abstract class HyriJoinPacket extends HyriPacket {

    protected final String targetServer;

    public HyriJoinPacket(String targetServer) {
        this.targetServer = targetServer;
    }

    public String getTargetServer() {
        return this.targetServer;
    }

}
