package fr.hyriode.api.proxy;

import fr.hyriode.api.application.IHyriApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/07/2021 at 09:54
 */
public interface IHyriProxy extends IHyriApplication<IHyriProxy.State> {

    /**
     * Get server players
     *
     * @return Server players
     */
    int getPlayers();

    /**
     * Get the proxy data
     *
     * @return A {@link HyggData}
     */
    HyggData getData();

    /**
     * The enumeration of all the states available for a proxy
     */
    enum State implements IHyriApplication.IState {

        /** Proxy is in creation */
        CREATING,
        /** Proxy is starting (onEnable in plugin) */
        STARTING,
        /** Proxy is ready to work and manage players */
        READY,
        /** Proxy is stopping (onDisable in plugin) */
        SHUTDOWN,
        /** Proxy is idling (an error occurred or just freezing) */
        IDLE

    }

}
