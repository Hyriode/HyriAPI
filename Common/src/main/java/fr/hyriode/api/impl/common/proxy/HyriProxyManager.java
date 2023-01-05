package fr.hyriode.api.impl.common.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.hyggdrasil.IHyggdrasilManager;
import fr.hyriode.api.proxy.IHyriProxyManager;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxiesRequester;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;

import java.util.*;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 10/05/2022 at 19:15
 */
public class HyriProxyManager implements IHyriProxyManager {

    private final Map<String, HyggProxy> proxies;

    private final IHyggdrasilManager hyggdrasilManager;

    public HyriProxyManager() {
        this.hyggdrasilManager = HyriAPI.get().getHyggdrasilManager();
        this.proxies = new HashMap<>();

        if (this.hyggdrasilManager.withHyggdrasil()) {
            for (HyggProxy proxy : this.hyggdrasilManager.getHyggdrasilAPI().getProxiesRequester().fetchProxies()) {
                this.proxies.put(proxy.getName(), proxy);
            }
        }
    }

    public void addProxy(HyggProxy proxy) {
        this.proxies.put(proxy.getName(), proxy);
    }

    public void removeProxy(HyggProxy proxy) {
        this.proxies.remove(proxy.getName());
    }

    @Override
    public Set<HyggProxy> getProxies() {
        return Collections.unmodifiableSet(new HashSet<>(this.proxies.values()));
    }

    @Override
    public HyggProxy getProxy(String name) {
        return this.proxies.get(name);
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
    public void waitForState(String proxyName, HyggProxy.State state, Consumer<HyggProxy> callback) {
        this.runActionOnRequester(requester -> requester.waitForProxyState(proxyName, state, callback));
    }

    @Override
    public void waitForPlayers(String proxyName, int players, Consumer<HyggProxy> callback) {
        this.runActionOnRequester(requester -> requester.waitForProxyPlayers(proxyName, players, callback));
    }

    private void runActionOnRequester(Consumer<HyggProxiesRequester> action) {
        if (HyriAPI.get().getConfig().withHyggdrasil()) {
            final HyggProxiesRequester requester = this.hyggdrasilManager.getHyggdrasilAPI().getProxiesRequester();

            if (requester != null) {
                action.accept(requester);
            }
        }
    }

}
