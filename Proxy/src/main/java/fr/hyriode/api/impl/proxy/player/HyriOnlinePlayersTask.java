package fr.hyriode.api.impl.proxy.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 11/05/2022 at 17:45
 */
public class HyriPlayersOnlineTask implements Runnable {

    private final List<UUID> players = new CopyOnWriteArrayList<>();

    public HyriPlayersOnlineTask(Plugin plugin) {
        ProxyServer.getInstance().getScheduler().schedule()
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

                HyriAPI.get().getPlayerManager().removePlayer(player);
                HyriAPI.get().getPlayerManager().sendPlayerToHydrion(account);
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
