package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 26/04/2023 at 15:07
 */
public class TemporarilySyncTask implements Runnable {

    public TemporarilySyncTask() {
        HyriAPI.get().getScheduler().schedule(this, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        final Set<UUID> badPlayers = new HashSet<>();

        for (UUID player : HyriAPI.get().getProxy().getPlayers()) {
            if (ProxyServer.getInstance().getPlayer(player) == null) {
                badPlayers.add(player);
            }
        }

        for (UUID player : badPlayers) {
            HyriAPI.get().getProxy().removePlayer(player);
        }
    }

}
