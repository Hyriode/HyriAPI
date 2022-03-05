package fr.hyriode.api.server;

import fr.hyriode.api.application.IHyriApplication;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public interface IHyriServer extends IHyriApplication<IHyriServer.State> {

    /**
     * Get server type
     *
     * @return Server type
     */
    String getType();

    /**
     * Get server players
     *
     * @return Server players
     */
    int getPlayers();

    /**
     * The enumeration of all the states available for a server
     */
    enum State implements IHyriApplication.IState {

        /** Server is in creation */
        CREATING,
        /** Server is starting (onEnable in plugin) */
        STARTING,
        /** Server is ready to host players */
        READY,
        /** Server is playing a game */
        PLAYING,
        /** Server is stopping (onDisable in plugin) */
        SHUTDOWN,
        /** Server is idling (an error occurred or just freezing) */
        IDLE

    }

}
