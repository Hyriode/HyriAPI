package fr.hyriode.api.network;

import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.api.network.event.HyriNetworkEventBus;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 16:47
 */
public interface IHyriNetworkManager {

    /**
     * Get the event bus used for the network.<br>
     * This event bus triggered events all over the network and events will be sent as packets.
     *
     * @return A {@link IHyriEventBus} instance
     */
    HyriNetworkEventBus getEventBus();

    /**
     * Get the network instance
     *
     * @return The {@link IHyriNetwork} instance
     */
    IHyriNetwork getNetwork();

    /**
     * Get the player counter
     *
     * @return The {@linkplain IHyriGlobalCounter global counter} object
     */
    IHyriGlobalCounter getPlayerCounter();

    /**
     * Set the network instance
     *
     * @param network New {@link IHyriNetwork} instance
     */
    void setNetwork(IHyriNetwork network);

}
