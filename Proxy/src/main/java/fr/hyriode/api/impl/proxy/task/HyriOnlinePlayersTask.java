package fr.hyriode.api.impl.proxy.task;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 11/05/2022 at 17:45<br>
 *
 * This task checks if a player is really online or not. Sometimes when a player is disconnected from an error, his account is not set as 'offline'.<br>
 * So to fix this issue, when a player is disconnected at the login from 'Already connected on this server' error, he will be added in this task and the task will check if the player is on a server.
 */
public class HyriOnlinePlayersTask implements Runnable {

    private final List<UUID> players = new CopyOnWriteArrayList<>();

    private ScheduledTask task;

    public void start(Plugin plugin) {
        HyriCommonImplementation.log("Starting online players checking task...");

        this.task = ProxyServer.getInstance().getScheduler().schedule(plugin, this, 0, 3, TimeUnit.SECONDS);
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    @Override
    public void run() {
        for (UUID player : this.players) {
            final IHyriPlayer account = IHyriPlayer.get(player);

            if (account == null) {
                continue;
            }

            if (account.isOnline() && !this.isOnline(player)) {
                account.setOnline(false);

                HyriAPI.get().getPlayerManager().removeCachedPlayer(player);
                HyriAPI.get().getPlayerManager().updatePlayer(account);
            }

            this.players.remove(player);
        }
    }

    public void addPlayerToCheck(UUID player) {
        if (this.players.contains(player)) {
            return;
        }

        this.players.add(player);
    }

    private boolean isOnline(UUID player) {
        for (HyggServer server : HyriAPI.get().getServerManager().getServers()) {
            if (server.getPlayers().contains(player)) {
                return true;
            }
        }
        return false;
    }

}
