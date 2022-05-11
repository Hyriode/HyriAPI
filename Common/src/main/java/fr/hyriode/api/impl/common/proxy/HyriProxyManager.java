package fr.hyriode.api.impl.common.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.proxy.IHyriProxyManager;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxyRequester;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxyState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 10/05/2022 at 19:15
 */
public class HyriProxyManager implements IHyriProxyManager {

    private final Map<String, HyggProxy> proxies;

    private final HyggdrasilManager hyggdrasilManager;

    public HyriProxyManager(HyriCommonImplementation implementation) {
        this.hyggdrasilManager = implementation.getHyggdrasilManager();
        this.proxies = new HashMap<>();
    }

    @Override
    public Collection<HyggProxy> getProxies() {
        return this.proxies.values();
    }

    @Override
    public HyggProxy getProxy(String name) {
        for (HyggProxy proxy : this.proxies.values()) {
            if (proxy.getName().equals(name)) {
                return proxy;
            }
        }
        return null;
    }

    @Override
    public void createProxy(Consumer<HyggProxy> onCreated) {
        this.runActionOnRequester(requester -> requester.createProxy(onCreated));
    }

    @Override
    public void removeProxy(String proxyName, Runnable onRemoved) {
        this.runActionOnRequester(requester -> requester.removeProxy(proxyName, onRemoved));
    }

    @Override
    public void waitForState(String proxyName, HyggProxyState state, Consumer<HyggProxy> callback) {
        this.runActionOnRequester(requester -> requester.waitForProxyState(proxyName, state, callback));
    }

    @Override
    public void waitForPlayers(String proxyName, int players, Consumer<HyggProxy> callback) {
        this.runActionOnRequester(requester -> requester.waitForProxyPlayers(proxyName, players, callback));
    }

    private void runActionOnRequester(Consumer<HyggProxyRequester> action) {
        if (HyriAPI.get().getConfiguration().withHyggdrasil()) {
            final HyggProxyRequester requester = this.hyggdrasilManager.getHyggdrasilAPI().getProxyRequester();

            if (requester != null) {
                action.accept(requester);
            }
        }
    }

}
