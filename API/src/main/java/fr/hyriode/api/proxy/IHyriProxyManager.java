package fr.hyriode.api.proxy;

import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 10/05/2022 at 18:50
 */
public interface IHyriProxyManager {

    /**
     * Get the collection of all proxies currently running
     *
     * @return A list of {@link HyggProxy}
     */
    Set<HyggProxy> getProxies();

    /**
     * Get the proxy object from its name
     *
     * @param name The name of the proxy to get
     * @return A {@link HyggProxy}
     */
    HyggProxy getProxy(String name);

    /**
     * Create a proxy with a given type
     *
     * @param onCreated The {@link Consumer} to call when the proxy will be created
     */
    void createProxy(Consumer<HyggProxy> onCreated);

    /**
     * Remove a proxy by giving its name
     *
     * @param proxyName The name of the proxy to remove
     * @param onRemoved The {@link Runnable} to run when the server will be removed
     */
    void removeProxy(String proxyName, Runnable onRemoved);

    /**
     * Wait for a proxy to have a given state
     *
     * @param proxyName The name of the proxy
     * @param state The state to wait for
     * @param callback The callback to fire when the proxy has the good state
     */
    void waitForState(String proxyName, HyggProxy.State state, Consumer<HyggProxy> callback);

    /**
     * Wait for a proxy to have a given amount of players
     *
     * @param proxyName The name of the proxy
     * @param players The amount of players to wait for
     * @param callback The callback to fire when the proxy has the good state
     */
    void waitForPlayers(String proxyName, int players, Consumer<HyggProxy> callback);

}