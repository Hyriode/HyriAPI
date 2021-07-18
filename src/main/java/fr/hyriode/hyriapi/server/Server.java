package fr.hyriode.hyriapi.server;

import fr.hyriode.hyggdrasilconnector.api.ServerState;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public abstract class Server {

    /**
     * Get server id
     *
     * @return Server id
     */
    public abstract String getId();

    /**
     * Get server started time
     *
     * @return Server started time
     */
    public abstract long getStartedTime();

    /**
     * Get server slots
     *
     * @return Server slots
     */
    public abstract int getSlots();

    /**
     * Set server slots
     *
     * @param slots - Server slots
     */
    public abstract void setSlots(int slots);

    /**
     * Get server players
     *
     * @return Server players
     */
    public abstract int getPlayers();

    /**
     * Set server players
     *
     * @param players - Server players
     */
    public abstract void setPlayers(int players);

    /**
     * Get server state
     *
     * @return Server state
     */
    public abstract ServerState getState();

    /**
     * Set server state
     *
     * @param state - Server state
     */
    public abstract void setState(ServerState state);

}
