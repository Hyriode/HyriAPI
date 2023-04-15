package fr.hyriode.api.server.packet;

import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

/**
 * Created by AstFaster
 * on 15/04/2023 at 11:08.<br>
 *
 * This packet is used to change the state of a server remotely.
 */
public class RemoteStateEditPacket extends HyriPacket {

    private final String targetServer;
    private final HyggServer.State newState;

    public RemoteStateEditPacket(String targetServer, HyggServer.State newState) {
        this.targetServer = targetServer;
        this.newState = newState;
    }

    public String getTargetServer() {
        return this.targetServer;
    }

    public HyggServer.State getNewState() {
        return this.newState;
    }

}
