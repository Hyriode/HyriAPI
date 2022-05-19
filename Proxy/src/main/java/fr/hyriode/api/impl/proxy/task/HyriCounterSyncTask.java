package fr.hyriode.api.impl.proxy.task;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.network.HyriNetworkCount;
import fr.hyriode.api.network.HyriPlayerCount;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/05/2022 at 09:11<br>
 *
 * This task updates the counter each 10 seconds to prevent from fake numbers in the counter.<br>
 * Ex: if a player is disconnected from an error, the proxy will sometimes not be aware, so the player will not be removed from the counter.
 */
public class HyriCounterSyncTask implements Runnable {

    private ScheduledTask task;

    public void start(Plugin plugin) {
        HyriCommonImplementation.log("Starting counter synchronisation task...");

        this.task = ProxyServer.getInstance().getScheduler().schedule(plugin, this, 10, 10, TimeUnit.SECONDS);
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    @Override
    public void run() {
        int totalPlayers = 0;
        for (HyggProxy proxy : HyriAPI.get().getProxyManager().getProxies()) {
            totalPlayers += proxy.getPlayers();
        }

        final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
        final Map<String, List<HyggServer>> servers = new HashMap<>();

        for (HyggServer server : HyriAPI.get().getServerManager().getServers()) {
            final String serverType = server.getType();
            final List<HyggServer> currentServers = servers.getOrDefault(serverType, new ArrayList<>());

            servers.put(serverType, currentServers);
        }

        final HyriNetworkCount playerCount = network.getPlayerCount();
        for (Map.Entry<String, List<HyggServer>> entry : servers.entrySet()) {
            this.updateServersCounter(playerCount.getCategory(entry.getKey()), entry.getValue());
        }

        playerCount.setPlayers(totalPlayers);
        network.update();
    }

    private void updateServersCounter(HyriPlayerCount playerCount, List<HyggServer> servers) {
        final Map<String, Integer> types = new HashMap<>();

        for (HyggServer server : servers) {
            final String gameType = server.getGameType();

            if (gameType != null) {
                types.put(gameType, types.getOrDefault(gameType, 0));
            }
        }

        for (Map.Entry<String, Integer> entry : types.entrySet()) {
            playerCount.setType(entry.getKey(), entry.getValue());
        }
    }

}
