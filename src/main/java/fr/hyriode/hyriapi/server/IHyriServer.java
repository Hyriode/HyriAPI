package fr.hyriode.hyriapi.server;

import fr.hyriode.hyggdrasilconnector.api.ServerState;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public interface IHyriServer {

    /**
     * Get server id
     *
     * @return Server id
     */
    String getId();

    /**
     * Get server started time
     *
     * @return Server started time
     */
    long getStartedTime();

    /**
     * Get server slots
     *
     * @return Server slots
     */
    int getSlots();

    /**
     * Set server slots
     *
     * @param slots - Server slots
     */
    void setSlots(int slots);

    /**
     * Get server players
     *
     * @return Server players
     */
    int getPlayers();

    /**
     * Set server players
     *
     * @param players - Server players
     */
    void setPlayers(int players);

    /**
     * Get server state
     *
     * @return Server state
     */
    ServerState getState();

    /**
     * Set server state
     *
     * @param state - Server state
     */
    void setState(ServerState state);

}
