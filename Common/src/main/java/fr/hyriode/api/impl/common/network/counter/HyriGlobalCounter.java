package fr.hyriode.api.impl.common.network.counter;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.network.counter.IHyriCategoryCounter;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AstFaster
 * on 08/07/2022 at 10:39
 */
public class HyriGlobalCounter implements IHyriGlobalCounter {

    private final Map<String, IHyriCategoryCounter> counters = new HashMap<>();

    @Override
    public int getPlayers() {
        int players = 0;
        for (HyggProxy proxy : HyriAPI.get().getProxyManager().getProxies()) {
            players += proxy.getPlayers().size();
        }
        return players;
    }

    @Override
    public IHyriCategoryCounter getCategory(String name) {
        final IHyriCategoryCounter categoryCounter = this.counters.getOrDefault(name, new HyriCategoryCounter(name));

        this.counters.putIfAbsent(name, categoryCounter);

        return categoryCounter;
    }

}
