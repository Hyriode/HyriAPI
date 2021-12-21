package fr.hyriode.hyriapi.pubsub;

import com.google.gson.Gson;

import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/11/2021 at 11:37
 */
public class HyriPacket {

    /** Packet json */
    private String json;

    /**
     * Set packet json.
     * This method is used after receiving packet
     *
     * @param json - Given json
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * Check if current packet instance of a given packet
     *
     * @param packetClass - Given packet class
     * @return - <code>true</code> if packet instance of given packet
     */
    public boolean is(Class<? extends HyriPacket> packetClass) {
        final Gson gson = new Gson();
        final HyriPacket packet = gson.fromJson(this.json, packetClass);

        return this.json.equals(gson.toJson(packet));
    }

    /**
     * Cast current packet to a given packet class type
     *
     * @param packetClass - The class of the packet to return
     * @param <T> - The given type
     * @return - Cast packet
     */
    public <T extends HyriPacket> T cast(Class<T> packetClass) {
        if (this.is(packetClass)) {
            return new Gson().fromJson(this.json, packetClass);
        }
        return null;
    }

    /**
     * Get the good packet in a list of classes, by testing if this packet
     * can be cast as a given class.
     *
     * @param packetsClasses - List of packets classes to test
     * @return - The good packet
     */
    public HyriPacket get(List<Class<? extends HyriPacket>> packetsClasses) {
        for (Class<? extends HyriPacket> packetClass : packetsClasses) {
            final HyriPacket packet = this.cast(packetClass);

            if (packet != null) {
                return packet;
            }
        }
        return null;
    }

}
