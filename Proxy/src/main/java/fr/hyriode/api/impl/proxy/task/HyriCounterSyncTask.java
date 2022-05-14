package fr.hyriode.api.impl.proxy.task;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

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
        int players = 0;
        for (HyggProxy proxy : HyriAPI.get().getProxyManager().getProxies()) {
            players += proxy.getPlayers();
        }

        final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

        network.getPlayerCount().setPlayers(players);
        network.update();
    }

}
