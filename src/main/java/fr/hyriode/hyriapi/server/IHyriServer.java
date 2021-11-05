package fr.hyriode.hyriapi.server;

import fr.hyriode.hyggdrasilconnector.api.ServerState;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public interface IHyriServer {

    /**
     * Get server name
     *
     * @return Server name
     */
    String getName();


    /**
     * Get server type
     *
     * @return Server type
     */
    String getType();

    /**
     * Get server started time
     *
     * @return Server started time
     */
    long getStartedTime();

    /**
     * Get server players
     *
     * @return Server players
     */
    int getPlayers();

    /**
     * Get server state
     *
     * @return Server state
     */
    ServerState getState();

}
