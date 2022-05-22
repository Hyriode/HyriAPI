package fr.hyriode.api.packet.model;

import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: Hyriode
 * Created by AstFaster
 * on 22/05/2022 at 08:31
 */
public class HyriEvacuateServerPacket extends HyriPacket {

    private final String from;
    private final String destination;

    public HyriEvacuateServerPacket(String from, String destination) {
        this.from = from;
        this.destination = destination;
    }

    public String getFrom() {
        return this.from;
    }

    public String getDestination() {
        return this.destination;
    }

}
