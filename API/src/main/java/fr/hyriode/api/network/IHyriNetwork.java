package fr.hyriode.api.network;

import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.network.event.HyriNetworkEventBus;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 08:32
 */
public interface IHyriNetwork {

    /**
     * Get the event bus used for the network.<br>
     * This event bus triggered events all over the network and events will be sent as packets.
     *
     * @return A {@link IHyriEventBus} instance
     */
    HyriNetworkEventBus getEventBus();

    /**
     * Get the amount of players that are currently connected on the network
     *
     * @return A player counter
     */
    int getPlayers();

    /**
     * Get the amount of available slots on the network
     *
     * @return A slot counter
     */
    int getSlots();

    /**
     * Set the amount of available slots on the network
     *
     * @param slots New slots amount
     */
    void setSlots(int slots);

    /**
     * Get the current motd of the network
     *
     * @return A motd
     */
    String getMotd();

    /**
     * Set the current motd of the network
     *
     * @param motd New motd
     */
    void setMotd(String motd);

    /**
     * Get the maintenance instance.<br>
     * {@link IHyriMaintenance} can be used to enable the maintenance or only to check if one is active
     *
     * @return The {@link IHyriMaintenance} instance
     */
    IHyriMaintenance getMaintenance();

}
