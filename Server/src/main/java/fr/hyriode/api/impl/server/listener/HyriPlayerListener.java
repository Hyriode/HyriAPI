package fr.hyriode.api.impl.server.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by Corentin
 * on 17/02/2022 at 18:36
 */
public class HyriPlayerListener implements Listener {

    private final HyggdrasilManager hyggdrasilManager;

    public HyriPlayerListener(HyggdrasilManager hyggdrasilManager) {
        this.hyggdrasilManager = hyggdrasilManager;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final UUID uuid = event.getUniqueId();

            IHyriPlayer player = playerManager.getPlayer(uuid);
            if (player == null) {
                player = playerManager.createPlayer(uuid, event.getName());
            }

            if (player != null) {
                return;
            }
        } catch (Exception ignored) {}

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "An error occurred while loading your profile!");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayer(player.getUniqueId());

        account.setName(player.getName());
        account.setLastLoginDate(new Date(System.currentTimeMillis()));

        playerManager.setPlayerId(account.getName(), account.getUUID());

        this.hyggdrasilManager.sendData();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayer(player.getUniqueId());

        account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));

        playerManager.sendPlayer(account);

        this.hyggdrasilManager.sendData();
    }

}
